package fr.mkadia.mkadiaapi.services.product;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.ProductMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import fr.mkadia.mkadiaapi.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final FileService fileService;
    @Override
    public Optional<ElementsOfPageDTO<ProductDTO>> getProducts(int page, int size) {
        return Optional.empty();
    }

    @Override
    public Optional<ResponseOperation<ProductDTO>> saveProduct(ProductDTO productDTO, List<MultipartFile> files) {
        Product product = productMapper.fromDTO(productDTO);
//        if (!files.isEmpty()){
//            List<Optional<String>> urls = files.stream().map(fileService::saveFile).toList();
//        }
        return Optional.of(
                ResponseOperation
                        .<ProductDTO>builder()
                        .message("product has been saved")
                        .object(productMapper.fromEntity(productRepository.save(product)))
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
