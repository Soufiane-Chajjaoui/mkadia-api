package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.services.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class FavorisController {

    private final FavoriteService favoriteService;

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToFavorites(
            @AuthenticationPrincipal User user,
            @PathVariable Integer productId
    ){
        favoriteService.addToFavorites(user, productId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> removeFromFavorites(
            @PathVariable Integer favoriteId
    ) {
        favoriteService.deleteFromFavorites(favoriteId);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(favoriteService.getFavorites(user));
    }
}
