package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.entities.OrderItem;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.enums.PaymentMethod;
import fr.mkadia.mkadiaapi.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminOrderDTO {
    private Integer id;
    private UserBasicDTO client;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private List<OrderItemSimpleDTO> items;
    private int countItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserBasicDTO delivery;
    private AddressBasic address;
    // ---- DTO imbriqués simplifiés ----

    @Data
    public static class AddressBasic {
        private String city;
        private String addressLine1;
        private int codePostal;
    }
}
