package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.Order;
import fr.mkadia.mkadiaapi.entities.OrderItem;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.OrderMapper;
import fr.mkadia.mkadiaapi.models.OrderItemRequest;
import fr.mkadia.mkadiaapi.repositories.OrderRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(User user, List<OrderItemRequest> itemRequests) {

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest req : itemRequests) {
            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));

            if (product.getStock() < req.getQuantity()) {
                throw new RuntimeException(STR."Stock insuffisant pour le produit : \{product.getName()}");
            }

            product.setStock(product.getStock() - req.getQuantity());
            productRepository.save(product);

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(req.getQuantity())
                    .price(product.getPrice())
                    .build();

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())));
            items.add(item);
        }

        // Affecter total et items à la commande
        order.setTotalAmount(total);
        order.setItems(items);

        // Sauvegarder la commande et cascade sur les items
        Order savedOrder = orderRepository.save(order);

        return orderMapper.fromEntity(savedOrder);
    }


    @Override
    public OrderDTO getOrder(Integer orderId) {
        return orderMapper.fromEntity(orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order introuvable")));
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        OrderDTO orderDTO = this.getOrder(orderId);
        orderDTO.setStatus(orderStatus);
        return orderMapper.fromEntity(orderRepository.save(orderMapper.fromDTO(orderDTO)));
    }
}
