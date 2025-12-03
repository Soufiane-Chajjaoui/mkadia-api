package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.dtos.OrderItemDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderItemService {
    public OrderItemDTO addOrderItem(OrderDTO order, ProductDTO product, int quantity);
    public void removeOrderItem(Long itemId);
    public List<OrderItemDTO> getItemsByOrder(Long orderId);
}