package fr.mkadia.mkadiaapi.services.favorite;

import fr.mkadia.mkadiaapi.dtos.FavoriteDTO;
import fr.mkadia.mkadiaapi.entities.Favorite;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.FavoriteMapper;
import fr.mkadia.mkadiaapi.repositories.FavoriteRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final ProductRepository productRepository;

    public FavoriteDTO addToFavorites(User user, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));

        Favorite favorite = Favorite.builder()
                .product(product)
                .user(user).build();

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
