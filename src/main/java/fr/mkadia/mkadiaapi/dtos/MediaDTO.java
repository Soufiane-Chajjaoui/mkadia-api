package fr.mkadia.mkadiaapi.dtos;

import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.enums.MediaType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDTO {
    private Long id;
    private String url;
    private MediaType type;
    private Product products;
    private Integer position;
}
