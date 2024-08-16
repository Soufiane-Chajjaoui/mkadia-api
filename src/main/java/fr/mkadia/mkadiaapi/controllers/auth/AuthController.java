package fr.mkadia.mkadiaapi.controllers.auth;


import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.AuthRequest;
import fr.mkadia.mkadiaapi.models.AuthResponse;
import fr.mkadia.mkadiaapi.models.PasswordRequest;
import fr.mkadia.mkadiaapi.services.authentication.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.of(authService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.of(authService.registerUser(userDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam(required = true, name = "id") Long id
            , @RequestBody PasswordRequest passwordRequest) {

        return ResponseEntity.of(authService.changePassword(id, passwordRequest));
    }
}
