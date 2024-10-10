package fr.mkadia.mkadiaapi.services.category;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.entities.Category;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.CategoryMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public Optional<CategoryDTO> getCategory(Long id) {
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
    public Optional<ResponseOperation<CategoryDTO>> addCategory(CategoryDTO categoryDTO) {
        Category categorySaved = categoryRepository.save(categoryMapper.fromDTO(categoryDTO));

        return Optional.of(
                ResponseOperation.<CategoryDTO>builder()
                        .message("Category Has been Registered")
                        .object(categoryMapper.fromEntity(categorySaved))
                        .build()
        );
    }

    @Override
    public Optional<ResponseMessage> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(STR."Category with id \{id} not found");
        }

        categoryRepository.deleteById(id);
        return Optional.of(
                ResponseMessage.builder()
                        .message("Has Been Deleted")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

}
