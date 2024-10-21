package fr.mkadia.mkadiaapi.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
