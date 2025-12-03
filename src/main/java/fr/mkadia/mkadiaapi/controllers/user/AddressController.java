package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.AddressDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.services.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAddresses(@AuthenticationPrincipal User user) {
        List<AddressDTO> addresses = addressService.getAddresses(user);
        return ResponseEntity.ok(addresses);
    }

    /**
     * POST /api/addresses - Créer une nouvelle adresse
     */
    @PostMapping
    public ResponseEntity<AddressDTO> saveAddress(
            @RequestBody AddressDTO addressDTO,
            @AuthenticationPrincipal User user
    ) {
        AddressDTO createdAddress = addressService.createAddress(addressDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    /**
     * DELETE /api/addresses/{id} - Supprimer une adresse
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        addressService.deleteAddress(id, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/addresses/{id}/default - Définir comme adresse par défaut
     */
    @PutMapping("/{id}/default")
    public ResponseEntity<AddressDTO> setDefaultAddress(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        AddressDTO address = addressService.setDefaultAddress(id, user);
        return ResponseEntity.ok(address);
    }
}
