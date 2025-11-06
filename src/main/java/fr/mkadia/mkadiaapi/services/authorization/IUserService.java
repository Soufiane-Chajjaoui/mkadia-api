package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.models.UpdateProfileRequest;
import org.springframework.http.ProblemDetail;

import java.util.Optional;
import java.util.Set;

public interface IUserService {
    Optional<ResponseOperation<UserDTO>> setRolesToUser(UserDTO userDTO, Set<RoleDTO> rolesDTO);
    Optional<User> getUserByEmail(String email);
    public Optional<ResponseOperation<UserDTO>> getCurrentUser(User user);
    ResponseOperation<?> updateProfile(UpdateProfileRequest request, User user);
}
