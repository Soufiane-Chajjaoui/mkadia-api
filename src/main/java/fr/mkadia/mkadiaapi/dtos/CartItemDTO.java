package fr.mkadia.mkadiaapi.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CartItemDTO {

    private Long id;
    private CartItemProductDTO product;
    private int quantity;
    private LocalDateTime  createdAt;
    private LocalDateTime updatedAt;
}
