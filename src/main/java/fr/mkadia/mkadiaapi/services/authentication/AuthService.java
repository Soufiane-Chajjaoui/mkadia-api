package fr.mkadia.mkadiaapi.services.authentication;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.TokenType;
import fr.mkadia.mkadiaapi.exceptions.EntityExistedException;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.RoleMapper;
import fr.mkadia.mkadiaapi.mappers.UserMapper;
import fr.mkadia.mkadiaapi.models.AuthRequest;
import fr.mkadia.mkadiaapi.models.AuthResponse;
import fr.mkadia.mkadiaapi.repositories.UserRepository;
import fr.mkadia.mkadiaapi.services.authorization.RoleService;
import fr.mkadia.mkadiaapi.services.jwt.IJwtService;
import fr.mkadia.mkadiaapi.services.jwt.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService{

    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ITokenService tokenService;

    @Override
    public Optional<AuthResponse> login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail() ,
                        authRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(()-> new EntityNotFoundException("Your Email not been Registered"));

        String accessToken = jwtService.generateToken(user);
        tokenService.saveUserToken(user , accessToken , TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveUserToken(user , refreshToken , TokenType.REFRESH);

        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Has been Logging").build();
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
}
