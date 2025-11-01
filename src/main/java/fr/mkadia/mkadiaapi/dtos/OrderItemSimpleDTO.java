package fr.mkadia.mkadiaapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemSimpleDTO {
    private Integer id;
    private int quantity;
    private ProductSimpleDTO product;
    private BigDecimal price;
}
