package fr.mkadia.mkadiaapi.services.product;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.entities.Media;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.enums.MediaType;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.ProductMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.MediaRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import fr.mkadia.mkadiaapi.services.file.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService{
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final MediaRepository mediaRepository;
    private final MinioStorageService minioStorageService;

    @Override
    public Optional<ElementsOfPageDTO<ProductDTO>> getProducts(int page, int size) {
        return Optional.empty();
    }

    @Override
    public Optional<ResponseOperation<ProductDTO>> saveProduct(ProductDTO productDTO, List<MultipartFile> files) {
        Product product = productMapper.fromDTO(productDTO);
        List<MinioStorageService.FileUploadRequest> fileRequests = files
                .stream()
                .map(file -> {
                    try {
                        return new MinioStorageService.FileUploadRequest(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        Product productSaved = productRepository.save(product);
        minioStorageService.uploadMultipleFiles(fileRequests)
                .forEach(mediaUrl -> {
                            String contentType = files.stream()
                                    .filter(file -> mediaUrl.contains(file.getOriginalFilename()))
                                    .findFirst()
                                    .map(MultipartFile::getContentType)
                                    .orElseThrow(()-> new IllegalStateException(STR."Content type not found for file :\{mediaUrl}"));
                            MediaType mediaType = contentType.startsWith("Video/") ? MediaType.VIDEO : MediaType.IMAGE;
                            mediaRepository.save(
                                    Media.builder()
                                            .products(productSaved)
                                            .url(mediaUrl)
                                            .type(mediaType)
                                            .build()
                            );
                        }
                );
        return Optional.of(
                ResponseOperation
                        .<ProductDTO>builder()
                        .message("product has been saved")
                        .object(productMapper.fromEntity(productSaved))
                        .build());
    }

    @Override
    public Optional<ProductDTO> getProduct(Integer id) {
        return Optional.of(
                productMapper.fromEntity(productRepository.findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Product Not Found")
                        )
                )
        );
    }

    @Override
    public Optional<ResponseMessage> deleteProduct(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<ResponseMessage> updateProduct(ProductDTO productDTO, List<MultipartFile> files) {
        return Optional.empty();
    }
}
