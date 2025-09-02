package fr.mkadia.mkadiaapi.controllers;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.dtos.mobile.ProductCardDTO;
import fr.mkadia.mkadiaapi.services.category.CategoryService;
import fr.mkadia.mkadiaapi.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/products/best-seller")
    public ResponseEntity<List<ProductCardDTO>> getBestSeller(){
        return ResponseEntity.of(productService.getBestSeller());
    }
}
