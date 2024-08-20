package fr.mkadia.mkadiaapi.services.authentication;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.AuthRequest;
import fr.mkadia.mkadiaapi.models.AuthResponse;
import fr.mkadia.mkadiaapi.models.PasswordRequest;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface IAuthService {

    Optional<AuthResponse> login(AuthRequest authRequestDTO) ;
    Optional<AuthResponse> registerUser(UserDTO UserDTO);

    @PreAuthorize("hasRole('USER')")
    Optional<ResponseOperation<String>> changePassword(Long idUser, PasswordRequest passwordRequest);
}
