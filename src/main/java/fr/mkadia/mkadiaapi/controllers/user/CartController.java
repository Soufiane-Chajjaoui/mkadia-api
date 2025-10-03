package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.CartDTO;
import fr.mkadia.mkadiaapi.models.CartItemRequest;
import fr.mkadia.mkadiaapi.entities.Cart;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.services.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @PostMapping("/add-item")
    public ResponseEntity<ResponseMessage> addItem(@AuthenticationPrincipal User user,
                                                      @RequestBody CartItemRequest request) {
        return ResponseEntity.of(cartService.addItem(user, request));
    }

    @PutMapping("/update-item")
    public ResponseEntity<CartDTO> updateItem(@AuthenticationPrincipal User user,
                                           @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(user, request));
    }

    @DeleteMapping("/delete-item/{cartItemId}")
    public ResponseEntity<CartDTO> deleteItem(@AuthenticationPrincipal User user,
                                           @PathVariable Integer cartItemId) {
        return ResponseEntity.ok(cartService.deleteItem(user, cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseMessage> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok(
                ResponseMessage.builder()
                        .message("Your Cart Has Been cleared")
                        .build()
        );
    }
}
