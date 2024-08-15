package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRoleService {
    Optional<RoleDTO> editRole(RoleDTO roleDTO);
    Optional<RoleDTO> addRole(RoleDTO roleDTO);
    Optional<RoleDTO> getRole(Long idRole);

    Optional<Set<RoleDTO>> getRolesIsDefault();

    Optional<Boolean> deleteRole(Long idRole);
    Optional<List<RoleDTO>> getRolesByIDs(List<Long> IDs);
}
