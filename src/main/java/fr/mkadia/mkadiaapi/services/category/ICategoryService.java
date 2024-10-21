package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import org.springframework.http.ProblemDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ICategoryService {

    Optional<CategoryDTO> getCategory(Long id);
    Optional<ElementsOfPageDTO<CategoryDTO>> getCategories(int page , int size , String keyword);

    Optional<ResponseOperation<CategoryDTO>> addCategory(CategoryDTO categoryDTO, MultipartFile file) throws IOException;

    Optional<ResponseMessage> deleteCategory(Long id);

    Optional<ResponseOperation<CategoryDTO>> updateCategory(CategoryDTO category, MultipartFile file) throws IOException;

}
