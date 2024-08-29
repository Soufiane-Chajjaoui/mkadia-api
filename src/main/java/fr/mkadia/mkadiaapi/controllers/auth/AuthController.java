package fr.mkadia.mkadiaapi.controllers.auth;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.*;
import fr.mkadia.mkadiaapi.services.authentication.IAuthService;
import fr.mkadia.mkadiaapi.services.jwt.ITokenService;
import fr.mkadia.mkadiaapi.services.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final ITokenService tokenService;
    private final MailService mailService;

    @PostMapping("/login")
    public ResponseEntity<ResponseOperation<String>> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.of(authService.login(authRequest));
    }
    @PostMapping(value = "/check-verification",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> checkVerification(@ModelAttribute CodeOTP codeOTP){
        return ResponseEntity.of(authService.checkVerification(codeOTP));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.of(authService.registerUser(userDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest) {

        return ResponseEntity.of(authService.changePassword(passwordRequest.getEmail(), passwordRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.of(tokenService.refreshToken(request, response));
    }

    @PostMapping(value = "/forget-password",
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseOperation<String>> processForgetPassword(@ModelAttribute PasswordRequest passwordRequest) throws UnknownHostException, MessagingException {
        mailService.sendResetPasswordEmail(passwordRequest.getEmail());
        return ResponseEntity.ok().body(ResponseOperation.<String>builder()
                .message("Mail Has Been Sent , Please Check-out .")
                .build()
        );
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ResponseOperation<UserDTO>> resetPassword(@RequestParam(name = "reset-token") String token){
        return ResponseEntity.ok().body(
                ResponseOperation.<UserDTO>builder()
                        .object(tokenService.tokenVerify(token))
                        .message("Token is Valid")
                        .build()
        );
    }
    @PatchMapping(value = "/change-reset-password",
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE ,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseOperation<String>> changeResetPassword(@ModelAttribute PasswordRequest resetPassword){
        authService.changeResetPassword(resetPassword);

        return ResponseEntity.ok().body(
                ResponseOperation.<String>builder()
                        .message("Your Password has been Updated")
                        .build()
        );
    }
}
