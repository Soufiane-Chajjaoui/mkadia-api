package fr.mkadia.mkadiaapi.services.jwt;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface ITokenService {

    void revokeTokens(User user, String currentRefreshToken);
    void saveUserToken(User appUser, String token, TokenType tokentype);

    Optional<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    UserDTO tokenVerify(String token);
}
