package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import io.minio.errors.*;
import org.springframework.http.ProblemDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Optional<CategoryDTO> getCategory(Integer id);
    Optional<ElementsOfPageDTO<CategoryDTO>> getCategories(int page , int size , String keyword);

    Optional<ResponseOperation<CategoryDTO>> addCategory(CategoryDTO categoryDTO, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    Optional<ResponseMessage> deleteCategory(Integer id);

    Optional<ResponseOperation<CategoryDTO>> updateCategory(CategoryDTO category, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    Optional<List<CategoryDTO>> getCategoriesByKeyword(String keyword);
}
