package fr.mkadia.mkadiaapi.dtos;


import fr.mkadia.mkadiaapi.enums.RoleLabel;
import lombok.*;

import java.util.Set;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class RoleDTO {
    private Long id;
    private RoleLabel label;
    private Boolean isDefault;
    private Set<UserDTO> users;
}
