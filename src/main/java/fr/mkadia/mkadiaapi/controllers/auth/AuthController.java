package fr.mkadia.mkadiaapi.controllers.auth;


import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.AuthRequest;
import fr.mkadia.mkadiaapi.models.AuthResponse;
import fr.mkadia.mkadiaapi.services.authentication.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        return ResponseEntity.of(authService.login(authRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.of(authService.registerUser(userDTO));
    }
}
