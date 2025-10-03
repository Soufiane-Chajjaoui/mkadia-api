package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.models.OrderItemRequest;

import java.util.List;

public interface IOrderService {
    public OrderDTO createOrder(User userDTO, List<OrderItemRequest> orderItemRequests);
    public OrderDTO getOrder(Integer orderId);
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus orderStatus);
}
