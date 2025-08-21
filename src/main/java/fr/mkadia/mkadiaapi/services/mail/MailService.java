package fr.mkadia.mkadiaapi.services.mail;

import fr.mkadia.mkadiaapi.config.ClientProperties;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.TokenType;
import fr.mkadia.mkadiaapi.helpers.UrlService;
import fr.mkadia.mkadiaapi.services.authorization.UserService;
import fr.mkadia.mkadiaapi.services.jwt.IJwtService;
import fr.mkadia.mkadiaapi.services.jwt.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@Slf4j
public class MailService {
    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final TokenService tokenService;
    private final IJwtService jwtService;
    private UrlService urlService;
    private final ClientProperties clientProperties;

    @Autowired
    public MailService(JavaMailSender mailSender , UserService userService , TokenService tokenService , IJwtService jwtService , UrlService urlService , ClientProperties clientProperties) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.urlService = urlService;
        this.clientProperties = clientProperties;
    }

    public void sendResetPasswordEmail(String to) throws UnknownHostException, MessagingException {
        User user = userService.getUserByEmail(to).orElseThrow(() -> new RuntimeException("User not found"));
        String resetToken = jwtService.generateResetToken(user);
        tokenService.revokeTokens(user , resetToken);
        tokenService.saveUserToken(user , resetToken , TokenType.RESET);
        String content = getContent(resetToken, user);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("Mkadia-App Reset Password");
        helper.setText(content, true);
        helper.setTo(to);
        helper.setFrom(fromMail);
        mailSender.send(mimeMessage);
    }

    private String getContent(String resetToken, User user) throws UnknownHostException {

        String webClientIpAddress = clientProperties.getIps().stream()
                .filter(client -> "web-client".equals(client.getName()))
                .map(ClientProperties.Client::getIp)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client 'web-client' not found"));

        String urlWithResetToken = String.format("%s/auth/reset/change-password?reset-token=%s", webClientIpAddress ,resetToken);

        String contentTemplate = "<p>Hi <i style='text-transform: uppercase;'>%s</i>,</p>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the button below to change your password:</p>" +
                "<br>" +
                "<a href='%s' style='background-color: #28a745; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; border-radius: 5px;margin: 10px 0 10px 10px'>Change my password</a>" +
                "<br>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<br><br>" +
                "<div style='border-top: 1px solid #ccc; width: 70%%; margin: 20px auto;'></div>" +  // Notez le '%%' ici
                "<div style='text-align: center;'>" +
                "<p>© 2024 [Mkadia App]. All rights reserved.</p>" +
                "<p>[Mkadia Application, LLC]<br>" +
                "4, allée du Béarn<br>" +
                "91300 - Massy<br>" +
                "France</p>" +
                "</div>" ;


        return String.format(contentTemplate, user.getLastName(), urlWithResetToken);
    }


}
