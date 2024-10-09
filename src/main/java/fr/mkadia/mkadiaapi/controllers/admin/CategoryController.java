package fr.mkadia.mkadiaapi.controllers.admin;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.services.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<ElementsOfPageDTO<CategoryDTO>> getCategories(@RequestParam(name = "page" , defaultValue = "0") int page,
                                                           @RequestParam(name = "keyword" ,required = false) String keyword,
                                                           @RequestParam(name = "size" , defaultValue = "5")int size){
        return ResponseEntity.of(categoryService.getCategories(page , size , keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id){
        return ResponseEntity.of(categoryService.getCategory(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.of(categoryService.addCategory(categoryDTO));
    }
}
