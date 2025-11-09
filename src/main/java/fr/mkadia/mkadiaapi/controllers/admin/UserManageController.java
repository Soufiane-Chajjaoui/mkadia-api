package fr.mkadia.mkadiaapi.controllers.admin;

import fr.mkadia.mkadiaapi.dtos.DeliveryManDTO;
import fr.mkadia.mkadiaapi.services.authorization.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserManageController {

    private final IUserService userService;

    @GetMapping("/delivery-men")
    public ResponseEntity<List<DeliveryManDTO>> getDeliveries() {
        return ResponseEntity.ok(userService.getDeliveries());
    }
}
