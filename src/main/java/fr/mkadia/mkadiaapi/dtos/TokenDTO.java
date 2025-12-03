package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.mkadia.mkadiaapi.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TokenDTO  {
    Long id;
    String token;
    boolean revoked;
    boolean expired;
    TokenType tokenType;
    @JsonIgnore
    UserDTO user;
}