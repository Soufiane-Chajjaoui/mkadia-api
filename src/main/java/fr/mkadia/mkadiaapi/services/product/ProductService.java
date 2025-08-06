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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService{
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final MediaRepository mediaRepository;
    private final MinioStorageService minioStorageService;

    @Override
    public Optional<ElementsOfPageDTO<ProductDTO>> getProducts(int page, int size, String keyword) {
        Page<Product> pageOfProducts = productRepository.findByNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
        Set<ProductDTO> productDTOs = pageOfProducts.stream().map(productMapper::fromEntity).collect(Collectors.toSet());

        ElementsOfPageDTO<ProductDTO> productsPage =               
                ElementsOfPageDTO.<ProductDTO>builder()
                .totalPages(pageOfProducts.getTotalPages())
                .pageSize(pageOfProducts.getSize())
                .totalRecords(pageOfProducts.getTotalElements())
                .currentPage(page)
                .elementsDTO(productDTOs)
                .build();
        return Optional.of(productsPage);
    }

    @Override
    public Optional<ResponseOperation<ProductDTO>> saveProduct(ProductDTO productDTO, List<MultipartFile> files) {
        Product product = productMapper.fromDTO(productDTO);
        Product productSaved = productRepository.save(product);
        AtomicInteger positionFile = new AtomicInteger(1);
        minioStorageService.uploadMultipleFiles(files)
                .forEach(mediaUrl -> {
                            String contentType = files.stream()
                                    .filter(file -> mediaUrl.contains(Objects.requireNonNull(file.getOriginalFilename())))
                                    .findFirst()
                                    .map(MultipartFile::getContentType)
                                    .orElseThrow(()-> new IllegalStateException(STR."Content type not found for file :\{mediaUrl}"));
                            MediaType mediaType = contentType.startsWith("Video/") ? MediaType.VIDEO : MediaType.IMAGE;
                            mediaRepository.save(
                                    Media.builder()
                                            .product(productSaved)
                                            .url(mediaUrl)
                                            .position(positionFile.getAndIncrement())
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
        ProductDTO product = getProduct(id).get();
        List<Media> mediaList = mediaRepository.findAllByProduct(productMapper.fromDTO(product));
        productRepository.deleteById(id);

        mediaList.forEach(media -> {
            minioStorageService.deleteObject(media.getUrl());
            mediaRepository.delete(media);
        });

        return Optional.of(
                ResponseMessage.builder()
                        .message("Has Been Deleted")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @Override
    public Optional<ResponseOperation<ProductDTO>> updateProduct(ProductDTO productDTO, List<MultipartFile> files) {

        log.info(productDTO.getName());
        log.info(String.valueOf(files.size()));
        // Vérifier si le produit existe
        Product productToUpdate = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product Not Found"));

        // Mise à jour des autres champs du produit
        productToUpdate.setName(productDTO.getName());
        productToUpdate.setDescription(productDTO.getDescription());
        productToUpdate.setPrice(productDTO.getPrice());

        // Gérer les médias (images)
        if (files != null && !files.isEmpty()) {
            // Récupérer les anciens médias
            List<Media> oldMediaList = mediaRepository.findAllByProduct(productToUpdate);
            Set<String> oldMediaUrls = oldMediaList.stream()
                    .map(Media::getUrl)
                    .collect(Collectors.toSet());

            Set<String> newFilesName = files.stream()
                    .map(MultipartFile::getOriginalFilename)
                    .collect(Collectors.toSet());

            oldMediaList.forEach((media) -> {
                String fileName = minioStorageService.extractObjectName(media.getUrl());
                if (!newFilesName.contains(fileName)) {
                    minioStorageService.deleteObject(fileName);
                    mediaRepository.delete(media);
                }
            });

            List<Media> mediaList =
            minioStorageService.uploadMultipleFiles(files)
                    .stream()
                    .map(mediaUrl -> {
                        String contentType = files.stream()
                                .filter(file -> mediaUrl.contains(file.getOriginalFilename()))
                                .findFirst()
                                .map(MultipartFile::getContentType)
                                .orElseThrow(() -> new IllegalStateException(STR."Content type not found for file: \{mediaUrl}"));

                        MediaType mediaType = contentType.startsWith("video/") ? MediaType.VIDEO : MediaType.IMAGE;

                        return Media.builder()
                                .product(productToUpdate)
                                .url(mediaUrl)
                                .type(mediaType)
                                .build();
                    }).toList();

            mediaRepository.saveAll(mediaList);
        }

        Product updatedProduct = productRepository.save(productToUpdate);

        return Optional.of(
                ResponseOperation.<ProductDTO>builder()
                        .message("Product has been updated successfully")
                        .object(productMapper.fromEntity(updatedProduct))
                        .build()
        );
    }

}
