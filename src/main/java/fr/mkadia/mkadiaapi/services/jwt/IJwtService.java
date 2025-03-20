package fr.mkadia.mkadiaapi.services.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface IJwtService {

    public String extractUsername(String token);
    public <T> T extractClaim(String token  , Function<Claims, T> claimsResolver);
    public Claims extractAllClaims(String token);
    public String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateResetToken(UserDetails userDetails);

    List<String> extractRoles(UserDetails userDetails);

    public boolean isTokenValid(String token , UserDetails userDetails);
    public boolean isTokenExpired(String token) ;
    public Date extractExpiration(String token);
    String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration);
    public SecretKey getSignInKey();

}
