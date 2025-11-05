package fr.mkadia.mkadiaapi.config;


import fr.mkadia.mkadiaapi.exceptions.CustomEntryPointHandler;
import fr.mkadia.mkadiaapi.services.authentication.LogoutService;
import fr.mkadia.mkadiaapi.services.filters.AuthFilterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthFilterService filterService;
    private final AuthenticationProvider authenticationProvider;
    private final CustomEntryPointHandler customEntryPointHandler;
    private final LogoutService logoutService;
    private final ClientProperties clientProperties;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers(
                                    "/api/v1/auth/login",
                                    "/api/v1/public/**",
                                    "/api/v1/auth/register",
                                    "/api/v1/auth/forgot-password",
                                    "/api/v1/auth/check-verification",
                                    "/api/v1/auth/reset-password",
                                    "/api/v1/auth/reset-password-mobile",
                                    "/v3/api-docs/**",         // ✅ autoriser toutes les sous-routes
                                    "/swagger-ui/**",          // ✅ Swagger UI
                                    "/swagger-ui.html"         // ✅ page principale Swagger
                            ).permitAll();
                            req.requestMatchers(HttpMethod.GET, "/api/v1/reviews").permitAll();
                            req.anyRequest()
                                    .authenticated();
                        }
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
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
        return httpSecurity.build();
    }

        @Bean
        public CorsConfigurationSource corsConfiguration() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(clientProperties.getIps().stream().map(ClientProperties.Client::getIp).collect(Collectors.toList()));
                configuration.addAllowedMethod("*");
                configuration.addAllowedHeader("*");
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
            return source;
        }

    @Bean
    public WebMvcConfigurer configurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedMethods("GET", "POST", "PUT", "DELETE" , "PATCH");
            }
        };
    }
}
