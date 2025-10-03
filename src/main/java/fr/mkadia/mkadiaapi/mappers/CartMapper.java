package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.CartDTO;
import fr.mkadia.mkadiaapi.entities.Cart;
import fr.mkadia.mkadiaapi.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {CartItemMapper.class, UserMapper.class})
public interface CartMapper {
    CartMapper MAPPER = Mappers.getMapper(CartMapper.class);
    CartDTO fromEntity(Cart cart);
    Cart fromDTO(CartDTO cartDTO);
}
