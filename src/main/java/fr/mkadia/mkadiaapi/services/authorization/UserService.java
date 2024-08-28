package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.RoleMapper;
import fr.mkadia.mkadiaapi.mappers.UserMapper;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final IRoleService roleService;
    @Override
    public Optional<ResponseOperation<UserDTO>> setRolesToUser(UserDTO userDTO, Set<RoleDTO> rolesDTO){
        User user = userMapper.fromDTO(userDTO);
        User userExisted = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("AppUser Not Found"));

        log.warn(String.valueOf(rolesDTO.size()));
        List<Long> rolesIds = rolesDTO.stream()
                .map(RoleDTO::getId)
                .toList();

        Set<Role> existingRoles = roleMapper.fromDTOs(new HashSet<RoleDTO>(roleService.getRolesByIDs(rolesIds).get()));

        if (CollectionUtils.isEmpty(existingRoles)) {

            throw new EntityNotFoundException("these roles not found in Database Or You're not Provide them, Please Check"); // Or log and return Optional.empty()
        }

        Set<Long> newRoleIds = new HashSet<>(rolesIds);
        newRoleIds.removeAll(existingRoles.stream().map(Role::getId).toList());

        if (!newRoleIds.isEmpty()) {
            Set<Role> newRolesDB = roleMapper.fromDTOs(
                    new HashSet<RoleDTO>(roleService.getRolesByIDs(new ArrayList<>(newRoleIds)).get())
            );
            if (CollectionUtils.isEmpty(newRolesDB)) {

                throw new EntityNotFoundException("New Roles not found for provided IDs"); // Or log and return Optional.empty()
            }

            existingRoles.addAll(newRolesDB);
        }

        user.setRoles(existingRoles);

        user.setPassword(userExisted.getPassword());
        user = userRepository.save(user);
        return Optional.of(
                ResponseOperation.<UserDTO>builder().object(userMapper
                        .fromEntity(user)).message("Roles has Been assigned").build()
        );
    }

    @Override
    public Optional<User> getUserByEmail(String email){

        User user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("This email not registered ou incorrect"));
        return Optional.of(user);
    }
}
