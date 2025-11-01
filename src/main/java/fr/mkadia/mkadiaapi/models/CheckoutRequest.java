package fr.mkadia.mkadiaapi.models;

import fr.mkadia.mkadiaapi.dtos.*;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {
    private BigDecimal totalAmount;
    private PaymentDTO payment;
    private AddressDTO address;
    private CouponDTO coupon;
    private DeliveryDTO delivery;
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING; // PENDING, PAID, SHIPPED, DELIVERED, CANCELED
    private Set<OrderItemDTO> items;
}
