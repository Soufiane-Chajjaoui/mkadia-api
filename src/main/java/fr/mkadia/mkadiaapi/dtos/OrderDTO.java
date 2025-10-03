package fr.mkadia.mkadiaapi.dtos;

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
    private Long orderId;
    private UserDTO user;
    private BigDecimal totalAmount;
    private OrderStatus status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELED
    private Set<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
