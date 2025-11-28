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
    private Integer userId;
    private Integer itemId;
    private String comment;
    private int rating;
    private Long timestamp;
}
