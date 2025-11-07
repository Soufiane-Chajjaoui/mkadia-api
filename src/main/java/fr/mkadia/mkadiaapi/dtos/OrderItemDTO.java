package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer orderItemId;
    private OrderDTO order;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal price; // prix unitaire
}
