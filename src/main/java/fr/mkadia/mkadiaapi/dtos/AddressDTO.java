package fr.mkadia.mkadiaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Integer id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private int codePostal;
    private String phone;
    private String label;
    @Builder.Default
    private Boolean isDefault = false;
}
