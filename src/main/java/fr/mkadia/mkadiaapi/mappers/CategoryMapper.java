package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.entities.Category;
import fr.mkadia.mkadiaapi.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category fromDTO(CategoryDTO categoryDTO);
    CategoryDTO fromEntity(Category category);
    Set<Category> fromDTOs(Set<CategoryDTO> categoryDTOSet);
}
