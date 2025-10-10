package fr.mkadia.mkadiaapi.models;

import fr.mkadia.mkadiaapi.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponResponse {
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountPercentage;
    private String message;
}
