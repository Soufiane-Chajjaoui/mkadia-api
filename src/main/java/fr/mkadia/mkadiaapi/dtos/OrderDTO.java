package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.mkadia.mkadiaapi.entities.Delivery;
import fr.mkadia.mkadiaapi.entities.Payment;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private UserDTO user;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private PaymentDTO payment;
    private AddressDTO address;
    private DeliveryDTO delivery;
    private OrderStatus status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELED
    private Set<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
