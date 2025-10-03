package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.CartItemProductDTO;
import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import fr.mkadia.mkadiaapi.dtos.ProductCardDTO;
import fr.mkadia.mkadiaapi.entities.Media;
import fr.mkadia.mkadiaapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {MediaMapper.class , CategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product fromDTO(ProductDTO productDTO);
    ProductDTO fromEntity(Product product);

    @Mapping(source = "discountPercentage", target = "discount")
    ProductCardDTO fromEntityToProductCard(Product product);

    @Mapping(source = "category", target = "category", ignore = true)
    @Named("fromEntityWithoutCategory")
    ProductDTO fromEntityWithoutCategory(Product product);

    @Mapping(source = "discountPercentage", target = "discount")
    @Mapping(source = "urls", target = "image", qualifiedByName = "getFirstMediaOfList")
    CartItemProductDTO fromEntityToCartItemProductDTO(Product product);

    @Named("getFirstMediaOfList")
    default String getFirstMediaOfList(List<Media> urls) {
        if (urls == null || urls.isEmpty()) return null;
        return urls.get(0).getUrl();
    }
}
