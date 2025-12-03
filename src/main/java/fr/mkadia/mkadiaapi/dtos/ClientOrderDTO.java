package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.enums.PaymentMethod;
import fr.mkadia.mkadiaapi.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientOrderDTO {
    private Long id;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private List<OrderItemSimpleDTO> items;
    private UserBasicDTO delivery;
    private int countItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressBasic address;

    @Data
    public static class AddressBasic {
        private String city;
        private String addressLine1;
        private int codePostal;
    }
}
