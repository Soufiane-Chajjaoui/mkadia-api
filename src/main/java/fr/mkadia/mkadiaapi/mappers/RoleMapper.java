package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring" , uses = {UserMapper.class})
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    Role fromDTO(RoleDTO roleDTO);
    RoleDTO fromEntity(Role role);
    Set<Role> fromDTOs(Set<RoleDTO> roleDTOs);
}
