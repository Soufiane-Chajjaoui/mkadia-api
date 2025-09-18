package fr.mkadia.mkadiaapi.controllers;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.dtos.mobile.ProductCardDTO;
import fr.mkadia.mkadiaapi.services.category.CategoryService;
import fr.mkadia.mkadiaapi.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return ResponseEntity.of(categoryService.getCategories());
    }

    @GetMapping("/top-products")
    public ResponseEntity<ElementsOfPageDTO<ProductCardDTO>> getTopProducts(@RequestParam(defaultValue = "ACTIVE") String status,
                                                                            @RequestParam(name = "page" , defaultValue = "0") int page,
                                                                            @RequestParam(name = "stock" ,required = false) int stock,
                                                                            @RequestParam(name = "size" , defaultValue = "5")int size) {
        return ResponseEntity.of(productService.getTopProducts(status, stock, page, size));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductsByCategory(@RequestParam(defaultValue = "ACTIVE") String status,
                                                   @RequestParam(name = "page", defaultValue = "0")int page,
                                                   @RequestParam(name = "stock", defaultValue = "1")int stock,
                                                   @RequestParam(name = "size", defaultValue = "5")int size,
                                                   @RequestParam(name = "category")Integer categoryId) {
        return ResponseEntity.of(productService.getProductsByCategory(status, page, size, stock, categoryId));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id")int id){
        return ResponseEntity.of(productService.getProduct(id));
    }
}
