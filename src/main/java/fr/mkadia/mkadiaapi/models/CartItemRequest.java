package fr.mkadia.mkadiaapi.models;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer productId;
    private Integer quantity;
}
