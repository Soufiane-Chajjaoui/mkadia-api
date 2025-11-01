package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.*;
import fr.mkadia.mkadiaapi.entities.*;
import fr.mkadia.mkadiaapi.enums.DeliveryStatus;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.StockInsuffisantException;
import fr.mkadia.mkadiaapi.mappers.OrderMapper;
import fr.mkadia.mkadiaapi.models.CheckoutRequest;
import fr.mkadia.mkadiaapi.repositories.*;
import fr.mkadia.mkadiaapi.services.coupon.CouponService;
import fr.mkadia.mkadiaapi.specifications.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    @Override
    public OrderDTO createOrder(User user, CheckoutRequest request) {
        log.info("Creating order for user: {}", user.getId());

        Order order = Order.builder()
                .user(user)
                .subTotal(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        // Traiter les items
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderItemDTO itemDTO : request.getItems()) {
                Product product = productRepository.findById(itemDTO.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Produit introuvable"));

                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new StockInsuffisantException(String.format(
                            "Stock insuffisant pour '%s'", product.getName()
                    ));
                }

                BigDecimal unitPrice = product.getPrice();
                if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
                    double discount = product.getDiscountPercentage() / 100.0;
                    unitPrice = unitPrice.multiply(BigDecimal.valueOf(1 - discount));
                }

                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
                subtotal = subtotal.add(itemTotal);

                product.setStock(product.getStock() - itemDTO.getQuantity());
                productRepository.save(product);

                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(itemDTO.getQuantity())
                        .price(unitPrice)
                        .build();

                order.addItem(orderItem);
            }
        }

        order.setSubTotal(subtotal);

        // 🔥 Appliquer le coupon si fourni
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getCoupon() != null && request.getCoupon().getCode() != null &&
                !request.getCoupon().getCode().trim().isEmpty()) {

            try {
                Coupon coupon = couponService.validateCoupon(request.getCoupon().getCode(), subtotal);
                discountAmount = couponService.calculateDiscount(coupon, subtotal);

                order.setCoupon(coupon);
                order.setDiscountAmount(discountAmount);

                log.info("✅ Coupon '{}' applied: -{} DH", coupon.getCode(), discountAmount);
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

        // Sauvegarder la commande
        order = orderRepository.save(order);
        log.info("✅ Order saved with ID: {} (Subtotal: {}, Discount: {}, Total: {})",
                order.getId(), subtotal, discountAmount, totalAmount);

        // Incrémenter l'utilisation du coupon
        if (order.getCoupon() != null) {
            couponService.incrementUsage(order.getCoupon());
        }

        // Créer le paiement avec le montant APRÈS réduction
        if (request.getPayment() != null) {
            Payment payment = Payment.builder()
                    .order(order)
                    .amount(totalAmount)
                    .method(request.getPayment().getMethod())
                    .build();
            order.setPayment(payment);
        }

        // Créer la livraison
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
                    .mode(request.getDelivery().getMode())
                    .status(DeliveryStatus.PENDING)
                    .build();
            order.setDelivery(delivery);
        }

        order = orderRepository.save(order);
        log.info("🎉 Order {} created successfully!", order.getId());

        return orderMapper.fromEntity(order);
    }

    @Override
    public Optional<AdminOrderDTO> getOrderDetails(Integer orderId) {
        log.info("Fetching order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Commande avec l'ID %d introuvable", orderId)
                ));

        return Optional.of(orderMapper.toOrderDetails(order));
    }

    @Override
    public OrderDTO getOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Commande avec l'ID %d introuvable", orderId)
                ));
        return orderMapper.fromEntity(order);
    }

    @Transactional
    @Override
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
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
}