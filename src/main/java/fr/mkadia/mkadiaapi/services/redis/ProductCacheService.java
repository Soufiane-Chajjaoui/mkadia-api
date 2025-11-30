package fr.mkadia.mkadiaapi.services.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCacheService {

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Duration TTL = Duration.ofHours(1);

    private static final String PRODUCT_KEY = "product:%d";
    private static final String PRODUCTS_PAGE_KEY = "products:page:%d:%d";

    // ---------------------------------------------------
    // 🔹 SINGLE PRODUCT
    // ---------------------------------------------------
    public Optional<ProductDTO> getCachedProduct(Integer id) {
        try {
            String key = PRODUCT_KEY.formatted(id);
            String json = redis.opsForValue().get(key);

            if (json == null) {
                return Optional.empty();
            }

            return Optional.of(
                    mapper.readValue(json, ProductDTO.class)
            );

        } catch (Exception e) {
            log.error("Failed to read product {} from cache", id, e);
            return Optional.empty();
        }
    }

    public void cacheProduct(Integer id, ProductDTO dto) {
        try {
            String key = PRODUCT_KEY.formatted(id);
            String json = mapper.writeValueAsString(dto);

            redis.opsForValue().set(key, json, TTL);

        } catch (Exception e) {
            log.error("Failed to cache product {}", id, e);
        }
    }

    // ---------------------------------------------------
    // 🔹 PAGINATED PRODUCTS
    // ---------------------------------------------------
    public Optional<ElementsOfPageDTO<ProductDTO>> getCachedProductsPage(int page, int size) {
        try {
            String key = PRODUCTS_PAGE_KEY.formatted(page, size);
            String json = redis.opsForValue().get(key);

            if (json == null) {
                return Optional.empty();
            }

            ElementsOfPageDTO<ProductDTO> dto =
                    mapper.readValue(json, new TypeReference<>() {});

            return Optional.of(dto);

        } catch (Exception e) {
            log.error("Failed to read cached product page {} {}", page, size, e);
            return Optional.empty();
        }
    }

    public void cacheProductsPage(int page, int size, ElementsOfPageDTO<ProductDTO> dto) {
        try {
            String key = PRODUCTS_PAGE_KEY.formatted(page, size);
            String json = mapper.writeValueAsString(dto);

            redis.opsForValue().set(key, json, TTL);

        } catch (Exception e) {
            log.error("Failed to cache products page {} {}", page, size, e);
        }
    }
}
