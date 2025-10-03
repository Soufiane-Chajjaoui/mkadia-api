package fr.mkadia.mkadiaapi.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CartDTO {

    private Integer id;
    private Set<CartItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
