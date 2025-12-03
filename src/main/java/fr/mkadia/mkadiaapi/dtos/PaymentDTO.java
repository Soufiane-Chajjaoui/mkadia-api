package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.mkadia.mkadiaapi.enums.PaymentMethod;
import fr.mkadia.mkadiaapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private OrderDTO order;
    private BigDecimal amount;
    private PaymentMethod method;
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
}
