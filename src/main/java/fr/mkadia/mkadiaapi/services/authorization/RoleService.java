package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.exceptions.EntityExistedException;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.RoleMapper;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Optional<ResponseOperation<RoleDTO>> editRole(RoleDTO roleDTO) {
        Optional<Role> role = Optional.ofNullable(roleMapper.fromDTO(roleDTO));

        role.get().setLabel(roleDTO.getLabel());
        return Optional.ofNullable(
                ResponseOperation.<RoleDTO>builder()
                        .object(roleMapper.fromEntity(roleRepository.save(role.get())))
                        .message(STR."\{roleDTO.getLabel()} Has been Edited")
                        .build()
        );
    }

    @Override
    public Optional<ResponseOperation<RoleDTO>> addRole(RoleDTO roleDTO) {

        Optional<Role> roleExisting = roleRepository.findFirstByLabelContainingOrderByLabelAsc(roleDTO.getLabel()) ;

        if (roleExisting.isPresent()){
            throw new EntityExistedException("This Role Already Existed");
        }

        Role role = roleMapper.fromDTO(roleDTO);
        role.setLabel(roleDTO.getLabel().toUpperCase());

        return Optional.ofNullable(
                ResponseOperation.<RoleDTO>builder()
                .object(roleMapper.fromEntity(roleRepository.save(role)))
                .message(STR."\{roleDTO.getLabel()} Has been Added")
                .build());
    }

    @Override
    public Optional<RoleDTO> getRole(Integer idRole) {

        Role role = roleRepository.findById(idRole)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Role of id : {0} Not Found", idRole))
                );

        return Optional.ofNullable(roleMapper.fromEntity(role));
    }
    @Override
    public Optional<Set<RoleDTO>> getDefaultRoles(){
        Set<Role> roles = roleRepository.findAllByIsDefaultTrue();
        return Optional.of(roles.stream().map(roleMapper::fromEntity).collect(Collectors.toSet()));
    }

    @Override
    public Optional<ResponseOperation<Boolean>> deleteRole(Integer idRole) {
        try {
            roleRepository.deleteById(idRole);
            return Optional.of(
                    ResponseOperation.<Boolean>builder()
                            .message("Role Has been Deleted").build()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<RoleDTO>> getRolesByIDs(List<Integer> IDs) {

        return Optional.of(
                roleRepository.findAllByIdIn(IDs)
                        .stream()
                        .map(roleMapper::fromEntity)
                        .toList()
        );
    }
}
