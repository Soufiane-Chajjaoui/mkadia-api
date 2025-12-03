package fr.mkadia.mkadiaapi.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryManDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
