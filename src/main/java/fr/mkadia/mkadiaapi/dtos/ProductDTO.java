package fr.mkadia.mkadiaapi.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private CategoryDTO category;
    private List<MediaDTO> urls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
