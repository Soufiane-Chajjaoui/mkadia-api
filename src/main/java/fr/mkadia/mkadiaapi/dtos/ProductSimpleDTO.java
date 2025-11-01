package fr.mkadia.mkadiaapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSimpleDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
}
