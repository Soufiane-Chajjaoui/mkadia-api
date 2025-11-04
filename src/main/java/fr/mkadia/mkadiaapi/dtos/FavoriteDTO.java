package fr.mkadia.mkadiaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Integer id;
//    private User user;
    private ProductSimpleDTO product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
