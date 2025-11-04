package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.mkadia.mkadiaapi.enums.ProductUnit;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductSimpleDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double discount;
    private int stock;
    private ProductUnit unit;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private Double quantity;
    private List<MediaDTO> urls;
}