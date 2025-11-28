package fr.mkadia.mkadiaapi.services.favorite;

import fr.mkadia.mkadiaapi.dtos.FavoriteDTO;
import fr.mkadia.mkadiaapi.dtos.FavoriteEvent;
import fr.mkadia.mkadiaapi.dtos.ReviewEvent;
import fr.mkadia.mkadiaapi.entities.Favorite;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.InteractionTopic;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.FavoriteMapper;
import fr.mkadia.mkadiaapi.repositories.FavoriteRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import fr.mkadia.mkadiaapi.services.stream.InteractionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final ProductRepository productRepository;
    private final InteractionProducer interactionProducer;
    public FavoriteDTO addToFavorites(User user, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));

        Favorite favorite = Favorite.builder()
                .product(product)
                .user(user).build();

        FavoriteEvent event = FavoriteEvent.builder()
                .productId(product.getId())
                .userId(user.getId())
                .timestamp(Instant.now().toEpochMilli())
                .build();

        interactionProducer.send(InteractionTopic.FAVORITE,event);
        return favoriteMapper.fromEntity(favoriteRepository.save(favorite));
    }

    public void deleteFromFavorites(Integer favoriteId) {
        favoriteRepository.findById(favoriteId).orElseThrow(()-> new EntityNotFoundException("Favorite not found"));
        favoriteRepository.deleteById(favoriteId);
    }

    public List<?> getFavorites(User user) {
        log.debug("Get favorites for user {}", user);
        PageRequest request = PageRequest.of(0, 10);
        List<FavoriteDTO> favoris = favoriteRepository.findByUserId(user.getId(), request)
                .map(favoriteMapper::fromEntity)
                .toList();
        return favoris;
    }
}
