package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" , uses = {RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User fromDTO(UserDTO userDTO);
    @Mapping(target = "password" , ignore = true)
    @Mapping(target = "tokens" , ignore = true)
    UserDTO fromEntity(User user);
}
