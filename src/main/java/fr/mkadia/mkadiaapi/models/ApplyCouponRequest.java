package fr.mkadia.mkadiaapi.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyCouponRequest {

    @NotBlank(message = "Le code promo est obligatoire")
    private String code;

    @NotNull(message = "Le montant du panier est obligatoire")
    @Positive(message = "Le montant du panier doit être positif")
    private BigDecimal cartAmount;
}