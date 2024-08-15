package fr.mkadia.mkadiaapi.services.authentication;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.AuthRequest;
import fr.mkadia.mkadiaapi.models.AuthResponse;

import java.util.Optional;

public interface IAuthService {

    Optional<AuthResponse> login(AuthRequest authRequestDTO) ;
    Optional<AuthResponse> registerUser(UserDTO UserDTO);

}
