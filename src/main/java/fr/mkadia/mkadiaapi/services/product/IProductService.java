package fr.mkadia.mkadiaapi.services.product;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.dtos.ProductCardDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IProductService {

    Optional<ElementsOfPageDTO<ProductDTO>> getProducts(String search,
                                                        Long categoryId,
                                                        BigDecimal minPrice,
                                                        BigDecimal maxPrice,
                                                        String status,
                                                        String stockStatus,
                                                        LocalDate createdAfter,
                                                        LocalDate createdBefore,
                                                        Pageable pageable);

    Optional<ElementsOfPageDTO<ProductCardDTO>> getTopProducts(String status,
                                                           int stock,
                                                           int page,
                                                           int size);

    Optional<ResponseOperation<ProductDTO>> saveProduct(ProductDTO productDTO, List<MultipartFile> files);

    Optional<ProductDTO> getProduct(Long id);

    Optional<ResponseMessage> deleteProduct(Long id);


    @Transactional
    Optional<ResponseOperation<ProductDTO>> updateProduct(ProductDTO productDTO,
                                                          List<MultipartFile> files,
                                                          List<String> existingUrls);

    Optional<?> getProductsByCategory(String status, int page, int size, int stock, Long categoryId);
}
