package fr.mkadia.mkadiaapi.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomEntryPointHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");

        Map<String, Object> responseBody = new HashMap<>();

        log.error("Error ::::: {} ::: {}", response.getStatus(), authException.getMessage());
        responseBody.put("timestamp", System.currentTimeMillis());
        responseBody.put("status", response.getStatus());
        responseBody.put("error", "Unauthorized");

        switch (response.getStatus()) {
            case 401:
                responseBody.put("message", "Your Session Has Been Expired. Go To Login");
                break;
            case 301:
                responseBody.put("message", "Provide me refresh token to reconstruct your new access token");
                responseBody.put("error", "Moved");
                responseBody.put("redirect", "http://localhost:8888/**/refresh-token");
                break;
            case 403:
                responseBody.put("message", STR."\{authException.getMessage()} Your token has been revoked");
                responseBody.put("error", "Unauthorized");
                break;
            default:
                responseBody.put("message", authException.getMessage());
                break;
        }
        responseBody.put("path", request.getRequestURI());
        responseBody.forEach((s, o) -> log.info("{} ::::: {}", s, o));
        response.getOutputStream().println(mapper.writeValueAsString(responseBody));
        log.info("Pre-authenticated entry point called. Rejecting access");
    }
}
