package fr.mkadia.mkadiaapi.services.product;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Optional<ElementsOfPageDTO<ProductDTO>> getProducts(int page, int size);

    Optional<ResponseOperation<ProductDTO>> saveProduct(ProductDTO productDTO, List<MultipartFile> files);

    Optional<ProductDTO> getProduct(Integer id);

    Optional<ResponseMessage> deleteProduct(Integer id);

    Optional<ResponseMessage> updateProduct(ProductDTO productDTO, List<MultipartFile> files);
}
