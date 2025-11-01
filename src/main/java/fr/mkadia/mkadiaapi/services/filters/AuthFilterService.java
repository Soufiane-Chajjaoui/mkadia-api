package fr.mkadia.mkadiaapi.services.filters;


import fr.mkadia.mkadiaapi.enums.TokenType;
import fr.mkadia.mkadiaapi.helpers.StatusCaptureResponseWrapper;
import fr.mkadia.mkadiaapi.repositories.TokenRepository;
import fr.mkadia.mkadiaapi.services.jwt.IJwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthFilterService extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;
    private final IJwtService jwtService;
    private final UserDetailsService userdetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String tokenType = request.getHeader("Token-Type");
        final String jwt;
        String email = null;
        StatusCaptureResponseWrapper responseWrapper = new StatusCaptureResponseWrapper(response);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            responseWrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
            filterChain.doFilter(request, responseWrapper);
            return;
        }

        jwt = authHeader.substring(7);
        var isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
        log.info(String.valueOf(isTokenValid));

        if (!isTokenValid) {
            responseWrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
            filterChain.doFilter(request, responseWrapper);
            return;
        }
        try {
            email = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {

            log.error(e.getMessage());
            log.info(STR."Token-Type ::\{tokenType}");
            responseWrapper.setStatus(HttpStatus.MOVED_PERMANENTLY.value());

            if (Objects.equals(tokenType, TokenType.REFRESH.name()) || Objects.equals(tokenType, TokenType.RESET.name())  ) {
                responseWrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
            }

            filterChain.doFilter(request, responseWrapper);
            return;
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userdetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt , userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request ,responseWrapper);
    }
}
