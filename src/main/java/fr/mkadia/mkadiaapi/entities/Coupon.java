package fr.mkadia.mkadiaapi.entities;

import fr.mkadia.mkadiaapi.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = true)
    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer usageLimit;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer usageCount = 0;

    private boolean active = true;
}