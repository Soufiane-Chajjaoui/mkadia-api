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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IAuthService authService;
    private final ITokenService tokenService;
    private final MailService mailService;
    private final TemplateEngine templateEngine;

    @PostMapping("/login")
    public ResponseEntity<ResponseOperation<?>> login(@RequestBody AuthRequest authRequest) {
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

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<ResponseOperation<String>> processForgetPassword(
            @RequestBody PasswordRequest passwordRequest,
            HttpServletRequest request) throws UnknownHostException, MessagingException {

        mailService.sendResetPasswordEmail(passwordRequest.getEmail(), request);
        return ResponseEntity.ok().body(ResponseOperation.<String>builder()
                .message("Mail Has Been Sent , Please Check-out .")
                .build()
        );
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ResponseOperation<UserDTO>> resetPassword(@RequestParam(name = "reset-token") String token){

        UserDTO user = tokenService.tokenVerify(token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseOperation.<UserDTO>builder()
                        .object(user)
                        .message("Token is Valid")
                        .build()
        );
    }

    @GetMapping("/reset-password-mobile")
    public ResponseEntity<String> mobileResetFallback(@RequestParam(name = "token") String token,
                                                      HttpServletRequest request) {
        try {
            log.info("🔗 Accès mobile reset avec token: {}",
                    STR."\{token.substring(0, Math.min(token.length(), 20))}...");

            // ✅ Vérifier le token
            UserDTO user = tokenService.tokenVerify(token);
            log.info("Token valide pour l'utilisateur: {}", user.getEmail());

            // ✅ Générer la page HTML avec Thymeleaf
            String htmlContent = generateMobileResetRedirectPage(token, user, request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(htmlContent);

        } catch (Exception e) {
            log.error("❌ Erreur token mobile reset: {}", e.getMessage());

            // Page d'erreur avec Thymeleaf
            String errorHtml = generateMobileErrorPage(e.getMessage(), request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(errorHtml);
        }
    }

    private String generateMobileResetRedirectPage(String token, UserDTO user, HttpServletRequest request) {
        Context context = new Context();

        // Variables utilisateur - Correction du problème de noms null
        String firstName = user.getFirstName() != null ? user.getFirstName() : "Utilisateur";
        String lastName = user.getLastName() != null ? user.getLastName() : "";

        context.setVariable("userName", firstName + " " + lastName);
        context.setVariable("userFirstName", firstName);
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("token", token);

        // Détection du device
        String userAgent = request.getHeader("User-Agent");
        boolean isAndroid = userAgent != null && userAgent.toLowerCase().contains("android");
        boolean isIOS = userAgent != null && (userAgent.toLowerCase().contains("iphone") ||
                userAgent.toLowerCase().contains("ipad"));

        context.setVariable("isAndroid", isAndroid);
        context.setVariable("isIOS", isIOS);
        context.setVariable("isMobile", isAndroid || isIOS);

        // URLs de deep linking avec email inclus
        String deepLinkUrl = String.format("mkadia://change-password?token=%s&email=%s",
                token, user.getEmail());
        String androidIntentUrl = String.format(
                "intent://change-password?token=%s&email=%s#Intent;scheme=mkadia;package=com.mkadia.app;S.browser_fallback_url=https://play.google.com/store/apps/details?id=com.mkadia.app;end",
                token, user.getEmail()
        );

        context.setVariable("deepLinkUrl", deepLinkUrl);
        context.setVariable("androidIntentUrl", androidIntentUrl);
        context.setVariable("androidStoreUrl", "https://play.google.com/store/apps/details?id=com.mkadia.app");
        context.setVariable("iosStoreUrl", "https://apps.apple.com/app/mkadia/id123456789");

        log.info("Génération page redirection mobile pour: {} - Nom: '{} {}'",
                user.getEmail(), firstName, lastName);

        return templateEngine.process("mobile/reset-password-redirect", context);
    }

    // ✅ Méthode pour générer la page d'erreur
    private String generateMobileErrorPage(String errorMessage, HttpServletRequest request) {
        Context context = new Context();
        context.setVariable("errorMessage", errorMessage);
        context.setVariable("supportEmail", "support@mkadia.com");

        String userAgent = request.getHeader("User-Agent");
        boolean isMobile = userAgent != null &&
                (userAgent.toLowerCase().contains("mobile") ||
                        userAgent.toLowerCase().contains("android") ||
                        userAgent.toLowerCase().contains("iphone"));

        context.setVariable("isMobile", isMobile);

        return templateEngine.process("mobile/reset-password-error", context);
    }


    @PatchMapping(value = "/change-reset-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseOperation<String>> changeResetPassword(
            @RequestBody PasswordRequest resetPassword
    ){

        authService.changeResetPassword(resetPassword);

        return ResponseEntity.ok().body(
                ResponseOperation.<String>builder()
                        .message("Your Password has been Updated")
                        .build()
        );
    }
}
