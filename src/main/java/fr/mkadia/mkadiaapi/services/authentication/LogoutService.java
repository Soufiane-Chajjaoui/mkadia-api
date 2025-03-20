package fr.mkadia.mkadiaapi.services.authentication;


import fr.mkadia.mkadiaapi.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoutService implements org.springframework.security.web.authentication.logout.LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String refreshToken =  request.getHeader("RefreshToken");
        final String token;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        token = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(token).orElse(null);
        var storedRefreshToken = tokenRepository.findByToken(refreshToken).orElse(null);
        if (storedToken != null && storedRefreshToken != null){
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            storedRefreshToken.setRevoked(true);
            storedRefreshToken.setExpired(true);
            tokenRepository.saveAll(List.of(storedToken,storedRefreshToken));
        }
    }
}
