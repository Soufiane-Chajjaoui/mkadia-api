package fr.mkadia.mkadiaapi.dtos;


import lombok.*;

import java.util.Set;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class RoleDTO {
    private Integer id;
    private String label;
    private Boolean isDefault;
    private Set<UserDTO> users;
}
