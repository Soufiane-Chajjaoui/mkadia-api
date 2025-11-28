package fr.mkadia.mkadiaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEvent {
    private Integer userId;
    private Integer productId;
    private Long timestamp;
}
