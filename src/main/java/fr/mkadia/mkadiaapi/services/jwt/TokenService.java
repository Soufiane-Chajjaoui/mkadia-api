package fr.mkadia.mkadiaapi.services.jwt;

import fr.mkadia.mkadiaapi.entities.Token;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.TokenType;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.models.ResponseRefreshToken;
import fr.mkadia.mkadiaapi.repositories.TokenRepository;
import fr.mkadia.mkadiaapi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService implements ITokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final IJwtService jwtService;

    @Override
    public void revokeTokens(User user) {
        List<Token> tokensValide = tokenRepository.findAllValidTokenByUser(user.getId());
        if (tokensValide.isEmpty())
            return;
        tokensValide.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(tokensValide);
    }

    @Override
    public void saveUserToken(User user, String jwtoken, TokenType tokentype) {

        Token token = Token.builder()
                .expired(false)
                .revoked(false)
                .tokenType(tokentype)
                .token(jwtoken)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public Optional<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String email;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return Optional.empty();
        }
        refreshToken = authHeader.substring(7);
        log.info(refreshToken);
        email = jwtService.extractUsername(refreshToken);
        var isRefreshTokenValid = tokenRepository.findByToken(refreshToken).map(t -> !t.isRevoked() && !t.isExpired())
                .orElse(false);
        if (isRefreshTokenValid) {
            if (email != null) {
                var user = userRepository.findByEmail(email).orElseThrow(
                        () -> new EntityNotFoundException("User Not Found")
                );
                if (jwtService.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtService.generateToken(user);
                    this.revokeTokens(user);
                    this.saveUserToken(user, accessToken, TokenType.ACCESS);
                    var refreshResponse = ResponseRefreshToken.builder()
                            .refreshToken(refreshToken)
                            .accessToken(accessToken)
                            .build();
                    return Optional.of(refreshResponse);
                }
            }
        }
        return Optional.ofNullable(new HashMap<>().put("message" , "Your Session Expired :("));
    }



}
