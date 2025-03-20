package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.entities.Category;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.CategoryMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.CategoryRepository;
import fr.mkadia.mkadiaapi.services.file.FileService;
import fr.mkadia.mkadiaapi.services.file.MinioStorageService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MinioStorageService minioStorageService;
    private final FileService fileService;
    @Override
    public Optional<CategoryDTO> getCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(STR."Category \{id} Not Found")
        );
        return Optional.of(
                categoryMapper.fromEntity(category)
        );
    }

    @Override
    public Optional<ElementsOfPageDTO<CategoryDTO>> getCategories(int page, int size, String keyword) {

        Page<Category> pageOfCategories = categoryRepository.findByNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
        Set<CategoryDTO> categoriesDTOs = pageOfCategories.stream().map(categoryMapper::fromEntity).collect(Collectors.toSet());
        ElementsOfPageDTO<CategoryDTO> categoriesPage =
                ElementsOfPageDTO.<CategoryDTO>builder()
                        .totalPages(pageOfCategories.getTotalPages())
                        .pageSize(pageOfCategories.getSize())
                        .totalRecords(pageOfCategories.getTotalElements())
                        .currentPage(page)
                        .elementsDTO(categoriesDTOs)
                        .build();
        return Optional.of(categoriesPage);
    }

    @Override
    public Optional<ResponseOperation<CategoryDTO>> addCategory(
            CategoryDTO categoryDTO,
            MultipartFile file){
        try {
            Optional<String> url = minioStorageService.uploadObject(file);
            if (url.isPresent()){
                categoryDTO.setUrl(url.get());
            }else throw new RuntimeException("File Not Upload");
            Category categorySaved = categoryRepository.save(categoryMapper.fromDTO(categoryDTO));
            return Optional.of(
                    ResponseOperation.<CategoryDTO>builder()
                            .message("Category Has been Registered")
                            .object(categoryMapper.fromEntity(categorySaved))
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<ResponseMessage> deleteCategory(Integer id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(STR."Category with id \{id} not found")
        );

        categoryRepository.deleteById(id);
        Optional<Boolean> deletedCategoryObject = minioStorageService.deleteObject(category.getUrl());
        deletedCategoryObject.orElseThrow(
                () -> new EntityNotFoundException(STR."Category with id \{id} not found")
        );
            return Optional.of(
                    ResponseMessage.builder()
                            .message("Has Been Deleted")
                            .status(HttpStatus.OK.value())
                            .build()
            );
    }
    @Override
    public Optional<ResponseOperation<CategoryDTO>> updateCategory(CategoryDTO category, MultipartFile file)
            throws IOException, ServerException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {

        Category categoryToUpdate = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found To update it"));

        if (file != null && !file.isEmpty()) {
            String oldUrl = categoryToUpdate.getUrl();

            if (oldUrl != null && !oldUrl.isEmpty()) {
                String oldObjectName = minioStorageService.extractObjectName(oldUrl);

                // Vérifier d'abord si le fichier a le même nom
                if (!oldObjectName.equals(file.getOriginalFilename())) {
                    // Vérifier si le fichier est réellement différent (taille ou contenu)
                    if (!minioStorageService.isSameObject(oldObjectName, file)) {
                        minioStorageService.deleteObject(oldObjectName);

                        Optional<String> newFileUrl = minioStorageService.uploadObject(file);
                        categoryToUpdate.setUrl(newFileUrl.orElseThrow(() ->
                                new IOException("File Upload Failed")));
                    }
                }
            } else {
                // Aucun fichier précédent, on upload directement
                Optional<String> newFileUrl = minioStorageService.uploadObject(file);
                categoryToUpdate.setUrl(newFileUrl.orElseThrow(() ->
                        new IOException("File Upload Failed")));
            }
        }

        // Mise à jour des autres champs
        categoryToUpdate.setName(category.getName());

        Category categoryUpdated = categoryRepository.save(categoryToUpdate);

        return Optional.of(
                ResponseOperation.<CategoryDTO>builder()
                        .message("Category Has been Updated")
                        .object(categoryMapper.fromEntity(categoryUpdated))
                        .build()
        );
    }

}
