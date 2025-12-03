package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.*;
import fr.mkadia.mkadiaapi.entities.*;
import fr.mkadia.mkadiaapi.enums.DeliveryStatus;
import fr.mkadia.mkadiaapi.enums.InteractionTopic;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.StockInsuffisantException;
import fr.mkadia.mkadiaapi.mappers.OrderMapper;
import fr.mkadia.mkadiaapi.models.CheckoutRequest;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.*;
import fr.mkadia.mkadiaapi.services.authorization.UserService;
import fr.mkadia.mkadiaapi.services.coupon.CouponService;
import fr.mkadia.mkadiaapi.services.stream.InteractionProducer;
import fr.mkadia.mkadiaapi.specifications.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;
    private final CouponService couponService;
    private final UserService userService;
    private final InteractionProducer interactionProducer;

    @Transactional
    @Override
    public OrderDTO createOrder(CheckoutRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Long> itemsIds = request.getItems()
                .stream().map(i-> i.getProduct().getId())
                .toList();
        log.info("📦 Creating order for user: {} ({})", user.getId(), user.getEmail());

        // Créer la commande
        Order order = Order.builder()
                .user(user)
                .subTotal(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        // ✅ Traiter les items
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            log.info("📦 Processing {} items", request.getItems().size());

            for (CheckoutItemDTO itemDTO : request.getItems()) {
                Product product = productRepository.findById(itemDTO.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Produit introuvable"));

                // Vérifier le stock
                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new StockInsuffisantException(String.format(
                            "Stock insuffisant pour '%s' (disponible: %d, demandé: %d)",
                            product.getName(),
                            product.getStock(),
                            itemDTO.getQuantity()
                    ));
                }

                // Calculer le prix avec réduction
                BigDecimal unitPrice = product.getPrice();
                if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
                    double discount = product.getDiscountPercentage() / 100.0;
                    unitPrice = unitPrice.multiply(BigDecimal.valueOf(1 - discount));
                }

                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
                subtotal = subtotal.add(itemTotal);

                // Décrémenter le stock
                product.setStock(product.getStock() - itemDTO.getQuantity());
                productRepository.save(product);

                // Créer l'OrderItem
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(itemDTO.getQuantity())
                        .price(unitPrice)
                        .build();

                order.addItem(orderItem);
            }
        }

        order.setSubTotal(subtotal);
        log.info("💰 Subtotal: {} DH", subtotal);

        // ✅ Appliquer le coupon
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getCoupon() != null &&
                request.getCoupon().getCode() != null &&
                !request.getCoupon().getCode().trim().isEmpty()) {

            try {
                Coupon coupon = couponService.validateCoupon(
                        request.getCoupon().getCode(),
                        subtotal
                );
                discountAmount = couponService.calculateDiscount(coupon, subtotal);

                order.setCoupon(coupon);
                order.setDiscountAmount(discountAmount);

                log.info("🎟️ Coupon '{}' applied: -{} DH", coupon.getCode(), discountAmount);
            } catch (Exception e) {
                log.warn("⚠️ Coupon validation failed: {}", e.getMessage());
                throw e;
            }
        }

        // Calculer le total final
        BigDecimal totalAmount = subtotal
                .subtract(discountAmount)                                      // on applique la remise
                .add(request.getDelivery().getMode().getFee());        // on ajoute les frais de livraison
        order.setTotalAmount(totalAmount);

        log.info("💵 Final total: {} DH (Subtotal: {} - Discount: {} + Delivery: {})",
                totalAmount, subtotal, discountAmount);

        // Sauvegarder la commande
        order = orderRepository.save(order);
        log.info("✅ Order saved with ID: {} (Subtotal: {}, Discount: {}, Total: {})",
                order.getId(), subtotal, discountAmount, totalAmount);

        // Incrémenter l'utilisation du coupon
        if (order.getCoupon() != null) {
            couponService.incrementUsage(order.getCoupon());
        }

        // ✅ Créer le paiement
        if (request.getPayment() != null) {
            Payment payment = Payment.builder()
                    .order(order)
                    .amount(totalAmount)
                    .method(request.getPayment().getMethod())
                    .status(request.getPayment().getStatus())
                    .build();
            order.setPayment(payment);
            log.info("💳 Payment created: {} (amount: {} DH)",
                    payment.getMethod(), payment.getAmount());
        }

        // ✅ Créer l'adresse et la livraison
        if (request.getAddress() != null && request.getDelivery() != null) {
            Address address = Address.builder()
                    .user(user)
                    .phone(request.getAddress().getPhone())
                    .addressLine1(request.getAddress().getAddressLine1())
                    .addressLine2(request.getAddress().getAddressLine2())
                    .city(request.getAddress().getCity())
                    .codePostal(request.getAddress().getCodePostal())
                    .build();
            address = addressRepository.save(address);

            Delivery delivery = Delivery.builder()
                    .order(order)
                    .address(address)
                    .mode(request.getDelivery().getMode()) // ✅ C'est un enum
                    .status(DeliveryStatus.PENDING)
                    .build();
            order.setDelivery(delivery);

            log.info("📍 Delivery created: {} to {}, {}",
                    delivery.getMode().name(), address.getCity(), address.getAddressLine1());
        }

        // Sauvegarder avec paiement et livraison
        order = orderRepository.save(order);
        log.info("🎉 Order {} created successfully!", order.getId());
        interactionProducer.send(InteractionTopic.ORDER,
                OrderEvent.builder()
                        .userId(user.getId())
                        .productIds(itemsIds)
                        .timestamp(Instant.now().toEpochMilli())
                        .build()
        );
        return orderMapper.fromEntity(order);
    }
    @Override
    public Optional<AdminOrderDTO> getOrderDetails(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Commande avec l'ID %d introuvable", orderId)
                ));

        return Optional.of(orderMapper.toOrderDetails(order));
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Commande avec l'ID %d introuvable", orderId)
                ));
        return orderMapper.fromEntity(order);
    }

    @Transactional
    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        log.info("Updating order {} status to {}", orderId, orderStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Commande avec l'ID %d introuvable", orderId)
                ));

        order.setStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        log.info("Order {} status updated successfully", orderId);

        return orderMapper.fromEntity(order);
    }

    @Override
    public ElementsOfPageDTO<AdminOrderDTO> getOrders(BigDecimal minAmount, BigDecimal maxAmount, String status, String paymentStatus, String paymentMethod, String search, LocalDate createAfter, LocalDate createBefore, Pageable pageable) {

        log.info("Fetching orders with filters - minAmount: {}, maxAmount: {}, status: {}, client: {}",
                minAmount, maxAmount, status, search);

        // Création de la spécification
        Specification<Order> spec = OrderSpecification.filterOrders(
                minAmount, maxAmount, createAfter, createBefore, status, paymentStatus, paymentMethod, search
        );

        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);

        List<AdminOrderDTO> orders = ordersPage.stream()
                .map(orderMapper::toAdminOrder)
                .toList();
        return ElementsOfPageDTO.<AdminOrderDTO>builder()
                .elementsDTO(orders)
                .currentPage(pageable.getPageNumber())
                .totalPages(ordersPage.getTotalPages())
                .totalRecords(ordersPage.getTotalElements())
                .pageSize(ordersPage.getSize())
                .build();
    }

    @Override
    public ElementsOfPageDTO<ClientOrderDTO> getClientOrders(User user, int page, int size) {
        Sort sortOrders = Sort.by(
                Sort.Direction.DESC, "createdAt"
        );
        PageRequest pageRequest = PageRequest.of(page, size, sortOrders);
        Page<Order> orders = orderRepository.findAllByUser(user, pageRequest);
        return  ElementsOfPageDTO.<ClientOrderDTO>builder()
                .elementsDTO(orders.stream()
                        .map(orderMapper::toClientOrder)
                        .toList())
                .currentPage(pageRequest.getPageNumber())
                .pageSize(orders.getSize())
                .totalRecords(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    @Override
    public ClientOrderDTO getClientOrderDetails(Long id) {
        return orderMapper.toClientOrderDetails(orderRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Commande avec l'ID %d introuvable", id))
        ));
    }

    @Override
    public ResponseMessage setDeliveryAssigned(Long orderId, DeliveryManDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(STR."Order with ID \{orderId} introuvable"));
        User deliveryMan = userService.getUserById(request.getId());
        Delivery delivery = order.getDelivery();
        delivery.setAssignedTo(deliveryMan);
        orderRepository.save(order);
        return ResponseMessage.builder()
                .message("Delivery man has been assigned")
                .build();
    }
}