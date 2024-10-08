package fr.mkadia.mkadiaapi.services.authentication;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.exceptions.EntityExistedException;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.PasswordIncorrectException;
import fr.mkadia.mkadiaapi.exceptions.VerificationException;
import fr.mkadia.mkadiaapi.mappers.UserMapper;
import fr.mkadia.mkadiaapi.models.*;
import fr.mkadia.mkadiaapi.repositories.UserRepository;
import fr.mkadia.mkadiaapi.services.authorization.RoleService;
import fr.mkadia.mkadiaapi.services.jwt.IJwtService;
import fr.mkadia.mkadiaapi.services.jwt.ITokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService {
    @Value("${twilio.service.ssid}")
    private String serviceSid;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ITokenService tokenService;


    public AuthService(RoleService roleService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, IJwtService jwtService, UserRepository userRepository, UserMapper userMapper, ITokenService tokenService) {
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }


    @Override
    public Optional<ResponseOperation<String>> login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Your Email not been Registered"));
        this.sendVerification(user.getPhone());
        ResponseOperation<String> response = ResponseOperation.<String>builder()
                .message("has been Send Verification Code")
                .object(user.getPhone())
                .build();
        return Optional.ofNullable(response);
    }

    @Override
    public Optional<AuthResponse> registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EntityExistedException(STR."\{userDTO.getEmail()} has already registered");
        }
        userDTO.setRoles(roleService.getDefaultRoles().get());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User userRegistered = userMapper.fromDTO(userDTO);
        log.info(userDTO.getPhone());
        userRepository.save(userRegistered);
        return Optional.ofNullable(AuthResponse.builder().message("Has Been Registered").build());
    }

    @Override
    public Optional<ResponseOperation<String>> changePassword(String mail, PasswordRequest passwordRequest) {
        if (passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {

            User user = userRepository.findByEmail(passwordRequest.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User Not FOUND"));
            if (passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
                userRepository.save(user);
                return Optional.of(ResponseOperation.<String>builder().message("Your Password Has Been Updated").build());
            }
        }
        throw new PasswordIncorrectException("Password is Not Correct ,Please Provide Correct Password");

    }

    @Override
    public void changeResetPassword(PasswordRequest passwordRequest) {
        if (passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            User user = userRepository.findByEmail(passwordRequest.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User Not FOUND"));

            user.setPassword(
                    passwordEncoder.encode(passwordRequest.getNewPassword())
            );
            userRepository.save(user);
            tokenService.revokeTokens(user);
        } else throw new PasswordIncorrectException("Password is Not Correct ,Please Provide Correct Password");
    }

    @Override
    public ResponseMessage sendVerification(String to) {
        try {
            Verification.creator(serviceSid, to, "sms")
                    .create();
            log.info("Send AuthMSG has Successfully");
        } catch (Exception exception) {
            throw new VerificationException(exception.getMessage());
        }
        return ResponseMessage.builder().message("has been Send Verification Code").status(HttpStatus.OK.value()).build();
    }

    @Override
    public Optional<AuthResponse> checkVerification(CodeOTP codeOTP) {

        log.info(codeOTP.getCode() + codeOTP.getTo());
        User user = userRepository.findByEmail(codeOTP.getTo())
                .orElseThrow(() -> new EntityNotFoundException("User Not FOUND"));
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setCode(codeOTP.getCode())
                    .setTo(user.getPhone())
                    .create();

            log.info(verificationCheck.getValid().toString());
            if (verificationCheck.getValid()) {
                String accessToken = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                return Optional.ofNullable(AuthResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .message("You're successfully authenticated")
                        .build()
                );
            }else throw new VerificationException("Code OTP Not Valid");

        } catch (Exception e) {
            throw new VerificationException("Code OTP Not Valid");
        }

    }
}
