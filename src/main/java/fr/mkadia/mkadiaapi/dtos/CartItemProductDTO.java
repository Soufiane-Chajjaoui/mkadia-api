package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.enums.ProductUnit;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
public class CartItemProductDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double discount;
    private int stock;
    private ProductUnit unit;
    private int quantity;
    private LocalDate expirationDate;
    private String image;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
