package fr.mkadia.mkadiaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserBasicDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
