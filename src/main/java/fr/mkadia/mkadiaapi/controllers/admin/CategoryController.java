package fr.mkadia.mkadiaapi.controllers.admin;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.services.category.ICategoryService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<ElementsOfPageDTO<CategoryDTO>> getCategories(@RequestParam(name = "page" , defaultValue = "0") int page,
                                                           @RequestParam(name = "keyword" ,required = false) String keyword,
                                                           @RequestParam(name = "size" , defaultValue = "5")int size){
        return ResponseEntity.of(categoryService.getCategories(page , size , keyword));
    }

    @GetMapping(params = {"keyword", "!page", "!size"})
    public ResponseEntity<List<CategoryDTO>> getCategoriesByKeyword(@RequestParam(name = "keyword" , required = false) String keyword){
        return ResponseEntity.of(categoryService.getCategoriesByKeyword(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id){
        return ResponseEntity.of(categoryService.getCategory(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseOperation<CategoryDTO>> addCategory(
            @RequestPart(name = "category") CategoryDTO categoryDTO,
            @RequestPart("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.of(categoryService.addCategory(categoryDTO , file));
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseOperation<CategoryDTO>> updateCategory(
            @RequestPart(name = "category") CategoryDTO categoryDTO,
            @RequestPart(name = "file", required = false) MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.of(categoryService.updateCategory(categoryDTO , file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        return ResponseEntity.of(categoryService.deleteCategory(id));
    }
}
