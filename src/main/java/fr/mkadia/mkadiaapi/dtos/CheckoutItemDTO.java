package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutItemDTO {
    private Integer id;
    private ProductSimpleDTO product;
    private int quantity;
}