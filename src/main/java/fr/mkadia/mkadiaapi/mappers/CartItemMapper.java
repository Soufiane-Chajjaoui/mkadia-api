package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.CartItemDTO;
import fr.mkadia.mkadiaapi.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {CartMapper.class, ProductMapper.class})
public interface CartItemMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
    CartItemDTO fromEntity(CartItem cartItem);
    CartItem fromDTO(CartItemDTO cartItemDTO);
    List<CartItemDTO> fromEntities(List<CartItem> items);
}
