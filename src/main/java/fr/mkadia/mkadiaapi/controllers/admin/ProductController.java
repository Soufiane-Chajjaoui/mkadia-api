package fr.mkadia.mkadiaapi.controllers.admin;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ElementsOfPageDTO<ProductDTO>> getProducts(@RequestParam(name = "page" , defaultValue = "0")int page,
                                                                    @RequestParam(name = "size" , defaultValue = "5")int size ){
        return ResponseEntity.of(productService.getProducts(page, size));
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseOperation<ProductDTO>> saveProduct(
            @RequestPart(name = "product") ProductDTO productDTO
            ,@RequestPart(name = "files" , required = false) List<MultipartFile> files){
        return ResponseEntity.of(productService.saveProduct(productDTO , files));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getCategory(@PathVariable Integer id){
        return ResponseEntity.of(productService.getProduct(id));
    }
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseMessage> updateCategory(
            @RequestPart(name = "product") ProductDTO productDTO,
            @RequestPart(name = "files" , required = false) List<MultipartFile> files
    ) throws IOException {
        return ResponseEntity.of(productService.updateProduct(productDTO , files));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        return ResponseEntity.of(productService.deleteProduct(id));
    }
}
