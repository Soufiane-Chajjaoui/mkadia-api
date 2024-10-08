package fr.mkadia.mkadiaapi.services.jwt;

import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
@Transactional
@Slf4j
@NoArgsConstructor
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.reset-token.expiration}")
    private long resetExpiration;


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> rolesExtracted = this.extractRoles(userDetails);
        claims.put("roles", rolesExtracted);
        return buildToken(claims, userDetails, this.jwtExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, this.refreshExpiration);
    }

    @Override
    public String generateResetToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> rolesExtracted = this.extractRoles(userDetails);
        claims.put("roles", rolesExtracted);
        return buildToken(claims, userDetails, this.resetExpiration);
    }

    @Override
    public List<String> extractRoles(UserDetails userDetails) {
        Optional<Set<Role>> roles = Optional.of(
                Optional.ofNullable(((User) userDetails).getRoles())
                        .orElse(Set.of(Role.builder().id(1L).build()))
        );

        return roles.get().stream()
                .map(Role::getLabel)
                .toList();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        long expirationTimeMillis = System.currentTimeMillis() + expiration;
        log.info(STR."Expiration Time: \{new Date(expirationTimeMillis)}");

        log.info(String.valueOf(expiration));
        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(expirationTimeMillis))
                .signWith(getSignInKey()).compact();
    }

    @Override
    public SecretKey getSignInKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }
}
