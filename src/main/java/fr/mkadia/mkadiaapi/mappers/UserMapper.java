package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.UserBasicDTO;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    UserDTO fromEntity(User user);
    User fromDTO(UserDTO userDTO);
}