package fr.mkadia.mkadiaapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer orderItemId;
    private OrderDTO order;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal price; // prix unitaire
}
