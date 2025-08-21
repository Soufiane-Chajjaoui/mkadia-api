package fr.mkadia.mkadiaapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class ResponseRefreshToken {
    private String refreshToken;
    private String accessToken;
}
