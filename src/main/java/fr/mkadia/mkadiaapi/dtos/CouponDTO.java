package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Data
@Builder
public class CouponDTO {
    private Integer id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
    private Integer usageCount = 0;
    private boolean active = true;
}
