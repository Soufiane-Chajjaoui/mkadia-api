package fr.mkadia.mkadiaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEvent {
    private Long userId;
    private Long itemId;
    private String comment;
    private int rating;
    private Long timestamp;
}
