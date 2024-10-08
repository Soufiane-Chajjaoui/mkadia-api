package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.models.ResponseOperation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRoleService {
    Optional<ResponseOperation<RoleDTO>> editRole(RoleDTO roleDTO);
    Optional<ResponseOperation<RoleDTO>> addRole(RoleDTO roleDTO);
    Optional<RoleDTO> getRole(Long idRole);

    Optional<Set<RoleDTO>> getDefaultRoles();

    Optional<ResponseOperation<Boolean>> deleteRole(Long idRole);
    Optional<List<RoleDTO>> getRolesByIDs(List<Long> IDs);
}
