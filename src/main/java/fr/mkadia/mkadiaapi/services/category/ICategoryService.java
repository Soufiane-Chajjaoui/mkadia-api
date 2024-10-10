package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;

import java.util.Optional;

public interface ICategoryService {

    Optional<CategoryDTO> getCategory(Long id);
    Optional<ElementsOfPageDTO<CategoryDTO>> getCategories(int page , int size , String keyword);

    Optional<ResponseOperation<CategoryDTO>> addCategory(CategoryDTO categoryDTO);

    Optional<ResponseMessage> deleteCategory(Long id);
}
