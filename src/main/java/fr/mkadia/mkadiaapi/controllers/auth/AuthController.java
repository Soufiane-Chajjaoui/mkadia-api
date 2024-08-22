package fr.mkadia.mkadiaapi.controllers.auth;


import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.*;
import fr.mkadia.mkadiaapi.services.authentication.IAuthService;
import fr.mkadia.mkadiaapi.services.jwt.ITokenService;
import fr.mkadia.mkadiaapi.services.verification.SmsService;
import fr.mkadia.mkadiaapi.services.verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final ITokenService tokenService;
    private final SmsService smsService;
    private final VerificationService verificationService;

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

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.of(tokenService.refreshToken(request, response));
    }
    @PostMapping(value = "/send-verification",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> sendVerification(@RequestParam("to") String to ){

        verificationService.sendVerification(to);
        return ResponseEntity.ok().body(ResponseMessage.builder().message("has been Send Verification Code").status(HttpStatus.OK.value()).build());
    }

    @PostMapping(value = "/check-verification",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> checkVerification(@ModelAttribute CodeOTP codeOTP){
        return ResponseEntity.ok().body(verificationService.checkVerification(codeOTP));
    }
}
