package fr.mkadia.mkadiaapi.services.product;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.dtos.mobile.ProductCardDTO;
import fr.mkadia.mkadiaapi.entities.Category;
import fr.mkadia.mkadiaapi.entities.Media;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.enums.MediaType;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.CategoryMapper;
import fr.mkadia.mkadiaapi.mappers.ProductMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.CategoryRepository;
import fr.mkadia.mkadiaapi.repositories.MediaRepository;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import fr.mkadia.mkadiaapi.services.file.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CategoryRepository categoryRepository;
    private final MinioStorageService minioStorageService;
    private final CategoryMapper categoryMapper;

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
    public Optional<ElementsOfPageDTO<ProductCardDTO>> getTopProducts(String status,
                                                                      int stock,
                                                                      int page,
                                                                      int size){
        PageRequest  pageRequest = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<Product> pageOfProducts = productRepository.findFeaturedProductsWithPagination(status.toUpperCase(), stock, pageRequest);

        ElementsOfPageDTO<ProductCardDTO> productsPage =
                ElementsOfPageDTO.<ProductCardDTO>builder()
                        .totalPages(pageOfProducts.getTotalPages())
                        .pageSize(pageOfProducts.getSize())
                        .totalRecords(pageOfProducts.getTotalElements())
                        .currentPage(page)
                        .elementsDTO(getSetOfProductsWithFirstMedia(pageOfProducts))
                        .build();
        return Optional.of(productsPage);
    }

    private Set<ProductCardDTO> getSetOfProductsWithFirstMedia(Page<Product> products){
        // Set first media for each product
        products.forEach(product -> {
            if (product.getUrls() != null && !product.getUrls().isEmpty()) {
                product.setUrls(List.of(product.getUrls().getFirst()));
            } else {
                product.setUrls(null);
            }
        });

        return products.stream().map(productMapper::fromEntityToProductCard).collect(Collectors.toSet());
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

        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));

        product.setUrls(mediaRepository.findAllByProduct(product));
        return Optional.of(
                productMapper.fromEntity(product)
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
    @Transactional
    @Override
    public Optional<ResponseOperation<ProductDTO>> updateProduct(ProductDTO productDTO,
                                                                 List<MultipartFile> files,
                                                                 List<String> existingUrls) {

        log.info("Updating product: {}", productDTO.getName());

        // 🔹 Charger le produit
        Product productToUpdate = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product Not Found"));

        // 🔹 Mise à jour des champs de base
        productToUpdate.setName(productDTO.getName());
        productToUpdate.setDescription(productDTO.getDescription());
        productToUpdate.setPrice(productDTO.getPrice());

        Category newCategory = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found"));
        productToUpdate.setCategory(newCategory);

        log.info("Before update: {}", productToUpdate);

        // 🔹 Récupérer tous les anciens médias
        List<Media> oldMediaList = mediaRepository.findAllByProduct(productToUpdate);

        // ------------------------------------------------------------
        // 1️⃣ SUPPRIMER LES MÉDIAS QUI NE SONT PAS DANS existingUrls
        // ------------------------------------------------------------
        if (existingUrls != null && !existingUrls.isEmpty()) {
            List<Media> toDelete = oldMediaList.stream()
                    .filter(media -> !existingUrls.contains(media.getUrl()))
                    .toList();

            if (!toDelete.isEmpty()) {
                toDelete.forEach(media -> {
                    String fileName = minioStorageService.extractObjectName(media.getUrl());
                    log.info("Deleting old media not present in request: {}", fileName);
                    minioStorageService.deleteObject(fileName);
                });
                mediaRepository.deleteAllInBatch(toDelete);
            }
        } else {
            // si aucune url envoyée → supprimer tous les anciens
            log.info("No existingUrls sent → deleting all old medias");
            oldMediaList.forEach(media -> {
                minioStorageService.deleteObject(minioStorageService.extractObjectName(media.getUrl()));
            });
            mediaRepository.deleteAllInBatch(oldMediaList);
        }

        // ------------------------------------------------------------
        // 2️⃣ AJOUTER LES NOUVEAUX FICHIERS
        // ------------------------------------------------------------
        if (files != null && !files.isEmpty()) {
            log.info("Uploading {} new files...", files.size());

            List<String> uploadedUrls = minioStorageService.uploadMultipleFiles(files);

            List<Media> newMediaList = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String uploadedUrl = uploadedUrls.get(i);

                MediaType mediaType = file.getContentType() != null && file.getContentType().startsWith("video/")
                        ? MediaType.VIDEO
                        : MediaType.IMAGE;

                Media media = Media.builder()
                        .product(productToUpdate)
                        .url(uploadedUrl)
                        .type(mediaType)
                        .build();

                newMediaList.add(media);
            }

            mediaRepository.saveAll(newMediaList);
        } else {
            log.info("No new files sent – keeping only existingUrls.");
        }

        // 🔹 Sauvegarder le produit final
        Product updatedProduct = productRepository.save(productToUpdate);

        return Optional.of(
                ResponseOperation.<ProductDTO>builder()
                        .message("Product has been updated successfully")
                        .object(productMapper.fromEntity(updatedProduct))
                        .build()
        );
    }

    @Override
    public Optional<ElementsOfPageDTO<ProductCardDTO>> getProductsByCategory(String status, int page, int size, int stock, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new EntityNotFoundException("Category not found"));

        Page<Product> productsOfPage = productRepository.findAvailableProductsByCategory(status, stock, category.getId(), PageRequest.of(page, size));


        ElementsOfPageDTO<ProductCardDTO> productsPage =
                ElementsOfPageDTO.<ProductCardDTO>builder()
                        .totalPages(productsOfPage.getTotalPages())
                        .pageSize(productsOfPage.getSize())
                        .totalRecords(productsOfPage.getTotalElements())
                        .currentPage(page)
                        .elementsDTO(getSetOfProductsWithFirstMedia(productsOfPage))
                        .build();
        return Optional.of(productsPage);
    }


}
