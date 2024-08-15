package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.exceptions.EntityExistedException;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.RoleMapper;
import fr.mkadia.mkadiaapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Optional<RoleDTO> editRole(RoleDTO roleDTO) {
        Optional<Role> role = Optional.ofNullable(roleMapper.fromDTO(roleDTO));

        role.get().setLabel(roleDTO.getLabel());
        return Optional.ofNullable(roleMapper.fromEntity(roleRepository.save(role.get())));
    }

    @Override
    public Optional<RoleDTO> addRole(RoleDTO roleDTO) {

        Optional<Role> roleExisting = roleRepository.findFirstByLabelContainingOrderByLabelAsc(roleDTO.getLabel()) ;

        if (roleExisting.isPresent()){
            throw new EntityExistedException("This Role Already Existed");
        }

        Role role = roleMapper.fromDTO(roleDTO);
        role.setLabel(roleDTO.getLabel().toUpperCase());

        return Optional.ofNullable(roleMapper.fromEntity(roleRepository.save(role)));
    }

    @Override
    public Optional<RoleDTO> getRole(Long idRole) {

        Role role = roleRepository.findById(idRole)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Role of id : {0} Not Found", idRole))
                );

        return Optional.ofNullable(roleMapper.fromEntity(role));
    }
    @Override
    public Optional<Set<RoleDTO>> getRolesIsDefault(){
        Set<Role> roles = roleRepository.findByIsDefaultTrue();
        return Optional.of(roles.stream().map(roleMapper::fromEntity).collect(Collectors.toSet()));
    }

    @Override
    public Optional<Boolean> deleteRole(Long idRole) {
        try {
            roleRepository.deleteById(idRole);
            return Optional.of(true);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<RoleDTO>> getRolesByIDs(List<Long> IDs) {

        return Optional.of(
                roleRepository.findAllByIdIn(IDs)
                        .stream()
                        .map(roleMapper::fromEntity)
                        .toList()
        );
    }
}
