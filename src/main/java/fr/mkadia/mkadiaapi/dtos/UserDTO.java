package fr.mkadia.mkadiaapi.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class UserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

//    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Le numéro de téléphone doit être valide.")
//    @Size(min = 7, max = 15, message = "Le numéro de téléphone doit contenir entre 7 et 15 caractères.")
    private String phone;

    private Set<RoleDTO> roles;
    private Set<TokenDTO> tokens;


}
