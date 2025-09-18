package fr.mkadia.mkadiaapi.services.mail;

import fr.mkadia.mkadiaapi.config.ClientProperties;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.TokenType;
import fr.mkadia.mkadiaapi.services.authorization.UserService;
import fr.mkadia.mkadiaapi.services.jwt.IJwtService;
import fr.mkadia.mkadiaapi.services.jwt.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class MailService {
    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${app.clients.web.reset-url}")
    private String webResetUrl;

    @Value("${app.clients.mobile.deep-link}")
    private String mobileDeepLink;

    @Value("${app.clients.mobile.fallback-url}")
    private String mobileFallbackUrl;

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final TokenService tokenService;
    private final IJwtService jwtService;
    private final TemplateEngine templateEngine; // ✅ Ajout Thymeleaf
    private final ClientProperties clientProperties;

    @Autowired
    public MailService(JavaMailSender mailSender, UserService userService, TokenService tokenService,
                       IJwtService jwtService, TemplateEngine templateEngine, ClientProperties clientProperties) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.templateEngine = templateEngine; // ✅ Injection Thymeleaf
        this.clientProperties = clientProperties;
    }

    public void sendResetPasswordEmail(String to, HttpServletRequest request) throws MessagingException {
        User user = userService.getUserByEmail(to).orElseThrow(() -> new RuntimeException("User not found"));
        String resetToken = jwtService.generateResetToken(user);
        tokenService.revokeTokens(user, resetToken);
        tokenService.saveUserToken(user, resetToken, TokenType.RESET);

        String clientType = detectClientType(request);
        log.info("Sending reset password email for client type: {} to user: {}", clientType, to);

        // ✅ Génération du contenu avec Thymeleaf
        String content = generateEmailContent(resetToken, user, clientType);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Headers pour améliorer la livraison
        mimeMessage.setHeader("X-Priority", "1");
        mimeMessage.setHeader("X-MSMail-Priority", "High");
        mimeMessage.setHeader("Importance", "High");

        helper.setText(content, true);
        helper.setSubject(getEmailSubject(clientType));
        helper.setTo(to);
        helper.setFrom(fromMail);

        // Log des URLs pour debug
        logGeneratedUrls(clientType, resetToken);

        mailSender.send(mimeMessage);
    }

    // ✅ Nouvelle méthode pour générer le contenu avec Thymeleaf
    private String generateEmailContent(String resetToken, User user, String clientType) {
        Context context = new Context();

        // ✅ Variables communes
        context.setVariable("userName", STR."\{user.getFirstName()} \{user.getLastName()}");
        context.setVariable("userFirstName", user.getFirstName());
        context.setVariable("resetToken", resetToken);

        // ✅ URLs selon le type de client
        if ("REACT_NATIVE".equals(clientType)) {
            String universalLinkUrl = String.format("%s?token=%s", mobileFallbackUrl, resetToken);
            String androidIntentUrl = String.format(
                    "intent://reset-password/%s#Intent;scheme=mkadia;package=com.mkadia.app;S.browser_fallback_url=%s;end",
                    resetToken, universalLinkUrl
            );

            context.setVariable("universalLinkUrl", universalLinkUrl);
            context.setVariable("androidIntentUrl", androidIntentUrl);
            context.setVariable("clientType", "mobile");

            return templateEngine.process("emails/reset-password-mobile", context);
        } else {
            String webClientIpAddress = clientProperties.getIps().stream()
                    .filter(client -> "web-client".equals(client.getName()))
                    .map(ClientProperties.Client::getIp)
                    .findFirst()
                    .orElse(webResetUrl);

            String resetUrl = String.format("%s?reset-token=%s", webClientIpAddress, resetToken);

            context.setVariable("resetUrl", resetUrl);
            context.setVariable("clientType", "web");

            return templateEngine.process("emails/reset-password-web", context);
        }
    }

    private void logGeneratedUrls(String clientType, String resetToken) {
        if ("REACT_NATIVE".equals(clientType)) {
            String universalLinkUrl = String.format("%s?token=%s", mobileFallbackUrl, resetToken);
            String androidIntentUrl = String.format(
                    "intent://reset-password/%s#Intent;scheme=mkadia;package=com.mkadia.app;S.browser_fallback_url=%s;end",
                    resetToken, universalLinkUrl
            );
            log.info("🔗 Lien universel généré : {}", universalLinkUrl);
            log.info("🤖 Intent Android généré : {}", androidIntentUrl);
        }
    }

    private String getEmailSubject(String clientType) {
        return switch (clientType) {
            case "REACT_NATIVE" -> "📱 Mkadia App - Réinitialisation de votre mot de passe";
            case "ANGULAR", "WEB" -> "🔒 Mkadia - Réinitialisation de votre mot de passe";
            default -> "Mkadia - Réinitialisation de votre mot de passe";
        };
    }

    private String detectClientType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String clientType = request.getHeader("X-Client-Type");
        String platform = request.getHeader("X-Platform");

        log.debug("Client detection - UserAgent: {}, ClientType: {}, Platform: {}", userAgent, clientType, platform);

        if ("react-native".equalsIgnoreCase(platform)) {
            return "REACT_NATIVE";
        } else if ("angular".equalsIgnoreCase(platform)) {
            return "ANGULAR";
        }

        if ("mobile".equalsIgnoreCase(clientType)) {
            return "REACT_NATIVE";
        } else if ("web".equalsIgnoreCase(clientType)) {
            return "ANGULAR";
        }

        if (userAgent != null) {
            if (userAgent.contains("React-Native")) {
                return "REACT_NATIVE";
            } else if (userAgent.contains("Angular")) {
                return "ANGULAR";
            } else if (userAgent.contains("okhttp") || userAgent.contains("CFNetwork")) {
                return "REACT_NATIVE";
            } else if (userAgent.contains("Mozilla") && userAgent.contains("Chrome")) {
                return "ANGULAR";
            }
        }
        return "WEB";
    }
}