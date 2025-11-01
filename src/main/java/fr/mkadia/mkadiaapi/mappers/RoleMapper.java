package fr.mkadia.mkadiaapi.mappers;

import ch.qos.logback.classic.spi.LoggingEventVO;
import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "users", ignore = true)  // 🔥 NE PAS mapper les users dans RoleDTO
    RoleDTO fromEntity(Role role);
    List<Role> fromDTOs(List<RoleDTO> rolesDtos);
    Role fromDTO(RoleDTO roleDTO);
}
