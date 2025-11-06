package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.services.authorization.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseOperation<UserDTO>> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.of(userService.getCurrentUser(user));
    }
}