package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;

import java.util.Optional;

public interface ICategoryService {

    Optional<CategoryDTO> getCategory(Long id);
    Optional<ElementsOfPageDTO<CategoryDTO>> getCategories(int page , int size , String keyword);

    Optional<CategoryDTO> addCategory(CategoryDTO categoryDTO);
}
