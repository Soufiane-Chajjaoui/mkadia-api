package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.AdminOrderDTO;
import fr.mkadia.mkadiaapi.dtos.ClientOrderDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.models.UpdateProfileRequest;
import fr.mkadia.mkadiaapi.services.authorization.IUserService;
import fr.mkadia.mkadiaapi.services.authorization.UserService;
import fr.mkadia.mkadiaapi.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;
    private final OrderService orderService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseOperation<UserDTO>> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.of(userService.getCurrentUser(user));
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<ResponseOperation<?>> updateProfile(@AuthenticationPrincipal User user,
                                                              @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request, user));
    }
    @GetMapping("/orders")
    public ResponseEntity<ElementsOfPageDTO<ClientOrderDTO>> getOrders(@AuthenticationPrincipal User user,
                                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getClientOrders(user, page, size));
    }
}