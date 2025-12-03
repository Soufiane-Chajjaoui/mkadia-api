package fr.mkadia.mkadiaapi.services.order;

import fr.mkadia.mkadiaapi.dtos.*;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.models.CheckoutRequest;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public interface IOrderService {
    OrderDTO createOrder(CheckoutRequest checkoutRequest);
    Optional<AdminOrderDTO> getOrderDetails(Long orderId);
    OrderDTO getOrder(Long orderId);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus orderStatus);
    ElementsOfPageDTO<AdminOrderDTO> getOrders(BigDecimal minAmount, BigDecimal maxAmount, String status, String paymentStatus, String paymentMethod, String client, LocalDate createAfter, LocalDate createBefore, Pageable pageable);
    ElementsOfPageDTO<ClientOrderDTO> getClientOrders(User user, int page, int size);
    ClientOrderDTO getClientOrderDetails(Long id);
    ResponseMessage setDeliveryAssigned(Long orderId, DeliveryManDTO request);
}
