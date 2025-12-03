package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.dtos.OrderItemDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.entities.OrderItem;
import fr.mkadia.mkadiaapi.mappers.OrderItemMapper;
import fr.mkadia.mkadiaapi.mappers.OrderMapper;
import fr.mkadia.mkadiaapi.mappers.ProductMapper;
import fr.mkadia.mkadiaapi.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final IOrderService orderService;
    @Override
    public OrderItemDTO addOrderItem(OrderDTO order, ProductDTO product, int quantity) {
        OrderItem orderItem = OrderItem.builder()
                .price(product.getPrice())
                .quantity(quantity)
                .product(productMapper.fromDTO(product))
                .order(orderMapper.fromDTO(order))
                .build();
        return orderItemMapper.fromEntity(orderItemRepository.save(orderItem));
    }

    @Override
    public void removeOrderItem(Long itemId) {
        orderItemRepository.deleteById(itemId);
    }

    @Override
    public List<OrderItemDTO> getItemsByOrder(Long orderId) {

        return orderItemMapper.fromEntities(
                orderItemRepository.findAllByOrder(
                        orderMapper.fromDTO(orderService.getOrder(orderId))
                )
        );
    }
}
