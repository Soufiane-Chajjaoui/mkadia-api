package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.ReviewDTO;
import fr.mkadia.mkadiaapi.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses =  {UserMapper.class, ProductMapper.class})
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "productId", source = "product.id")
    ReviewDTO fromEntity(Review entity);

    @Mapping(target = "product.id", source = "productId")
    Review fromDTO(ReviewDTO reviewDTO);
}
