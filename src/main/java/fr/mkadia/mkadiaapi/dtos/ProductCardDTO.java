package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCardDTO {

    private Long id;
    private String name;
    private BigDecimal  price;
    private Double discount;
    private ProductUnit unit;
    private Double quantity;
    private List<MediaDTO> urls;
}
