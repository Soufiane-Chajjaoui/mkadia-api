package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.AddressDTO;
import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.models.CheckoutRequest;
import fr.mkadia.mkadiaapi.services.order.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Tag(name = "Checkout", description = "API de gestion des commandes")  // ⚠️ AJOUTEZ CECI
public class CheckoutController {

    private final IOrderService orderService;

    @PostMapping("/confirm")
    @Operation(summary = "Créer une commande", description = "Permet de créer une nouvelle commande")  // ⚠️ AJOUTEZ CECI
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<OrderDTO> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody CheckoutRequest checkoutRequest) {
        OrderDTO order = orderService.createOrder(user, checkoutRequest);
        return ResponseEntity.ok(order);
    }

}