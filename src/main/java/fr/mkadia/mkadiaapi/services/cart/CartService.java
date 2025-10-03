package fr.mkadia.mkadiaapi.services.cart;

import fr.mkadia.mkadiaapi.dtos.CartDTO;
import fr.mkadia.mkadiaapi.entities.*;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.CartMapper;
import fr.mkadia.mkadiaapi.models.CartItemRequest;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.repositories.CartItemRepository;
import fr.mkadia.mkadiaapi.repositories.CartRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;

    public CartDTO getCart(User user) {
        return cartMapper.fromEntity(cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build())));
    }

    @Transactional
    public Optional<ResponseMessage> addItem(User user, CartItemRequest request) {
        Cart cart = cartMapper.fromDTO(getCart(user));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("product not found"));

        if (cart.getItems() == null) {
            cart.setItems(new HashSet<>());
        }
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(item);
            this.cartItemRepository.save(item);
        }
        return Optional.of(
                ResponseMessage.builder()
                        .message("Item Has been Registered")
                        .status(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @Transactional
    public CartDTO updateItem(User user, CartItemRequest request) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        cart.getItems().forEach(item -> {
            if (item.getProduct().getId().equals(request.getProductId())) {
                item.setQuantity(request.getQuantity());
                item.setUpdatedAt(LocalDateTime.now());
            }
        });

        cart.setUser(user);

        return cartMapper.fromEntity(cartRepository.save(cart));
    }


    @Transactional
    public CartDTO deleteItem(User user, Integer cartItemId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        cart.getItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));

        cart.setUser(user);

        return cartMapper.fromEntity(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = cartMapper.fromDTO(getCart(user));
        cart.getItems().clear();
        cart.setUser(user);
        cartRepository.save(cart);
    }
}
