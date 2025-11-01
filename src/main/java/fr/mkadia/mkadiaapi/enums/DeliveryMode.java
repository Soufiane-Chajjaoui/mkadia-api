package fr.mkadia.mkadiaapi.enums;

import java.math.BigDecimal;

public enum DeliveryMode {
    FAST(new BigDecimal("3.00")),      // Livraison rapide (ex. < 30 min)
    STANDARD(new BigDecimal("0.00")),  // Livraison classique
    PICKUP(new BigDecimal("1.50"));    // Retrait en magasin

    private final BigDecimal fee;  // frais associé à la méthode

    DeliveryMode(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFee() {
        return fee;
    }
}
