package fr.mkadia.mkadiaapi.services.authentication;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.models.*;

import java.util.Optional;

public interface IAuthService {

    Optional<ResponseOperation<?>> login(AuthRequest authRequestDTO) ;
    Optional<AuthResponse> registerUser(UserDTO UserDTO);
    Optional<ResponseOperation<String>> changePassword(String idUser, PasswordRequest passwordRequest);
    void changeResetPassword(PasswordRequest passwordRequest);

    ResponseMessage sendVerification(String to);

    Optional<AuthResponse> checkVerification(CodeOTP codeOTP);

    Optional<ResponseOperation<UserDTO>> getCurrentUser();
}
