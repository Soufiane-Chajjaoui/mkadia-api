package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
@Mapper(componentModel = "spring" , uses = {MediaMapper.class , CategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product fromDTO(ProductDTO productDTO);
    ProductDTO fromEntity(Product product);
    Set<Product> fromDTOs(Set<ProductDTO> productDTOS);
}
