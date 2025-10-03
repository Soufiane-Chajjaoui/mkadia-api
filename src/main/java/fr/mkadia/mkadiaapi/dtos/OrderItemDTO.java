package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.entities.Order;
import fr.mkadia.mkadiaapi.entities.Product;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer orderItemId;
    private OrderDTO order;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal price; // prix unitaire
}
