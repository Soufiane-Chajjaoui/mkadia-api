package fr.mkadia.mkadiaapi.config;


import fr.mkadia.mkadiaapi.exceptions.CustomEntryPointHandler;
import fr.mkadia.mkadiaapi.services.authentication.LogoutService;
import fr.mkadia.mkadiaapi.services.filters.AuthFilterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthFilterService filterService;
    private final AuthenticationProvider authenticationProvider;
    private final CustomEntryPointHandler customEntryPointHandler;
    private final LogoutService logoutService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers("/api/v1/auth/login", "/api/v1/auth/register", "/v3/api-docs", "/swagger-ui/**")
                                    .permitAll();
                            req.anyRequest()
                                    .authenticated();
                        }
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filterService , UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(customEntryPointHandler)
                )
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutService)
                                .logoutSuccessHandler((((_, response, _) -> {
                                    SecurityContextHolder.clearContext();
                                    response.setStatus(HttpServletResponse.SC_OK);
                                }))))
        ;
        return httpSecurity.build() ;
    }

}
