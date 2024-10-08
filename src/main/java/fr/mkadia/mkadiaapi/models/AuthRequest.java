package fr.mkadia.mkadiaapi.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class AuthRequest {
    private String email;
    private String password;
}
