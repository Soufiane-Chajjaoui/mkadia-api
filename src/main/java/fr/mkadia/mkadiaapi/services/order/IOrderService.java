package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.AdminOrderDTO;
import fr.mkadia.mkadiaapi.dtos.ClientOrderDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.models.CheckoutRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface IOrderService {
    OrderDTO createOrder(CheckoutRequest checkoutRequest);
    Optional<AdminOrderDTO> getOrderDetails(Integer orderId);
    OrderDTO getOrder(Integer orderId);
    OrderDTO updateOrderStatus(Integer orderId, OrderStatus orderStatus);
    ElementsOfPageDTO<AdminOrderDTO> getOrders(BigDecimal minAmount, BigDecimal maxAmount, String status, String paymentStatus, String paymentMethod, String client, LocalDate createAfter, LocalDate createBefore, Pageable pageable);
    ElementsOfPageDTO<ClientOrderDTO> getClientOrders(User user, int page, int size);
}
