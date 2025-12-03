package fr.mkadia.mkadiaapi.services.authorization;

import fr.mkadia.mkadiaapi.dtos.DeliveryManDTO;
import fr.mkadia.mkadiaapi.dtos.RoleDTO;
import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.RoleLabel;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.RoleMapper;
import fr.mkadia.mkadiaapi.mappers.UserMapper;
import fr.mkadia.mkadiaapi.models.ResponseOperation;
import fr.mkadia.mkadiaapi.models.UpdateProfileRequest;
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

        List<Role> existingRoles = roleMapper.fromDTOs(roleService.getRolesByIDs(rolesIds).get());

        if (CollectionUtils.isEmpty(existingRoles)) {

            throw new EntityNotFoundException("these roles not found in Database Or You're not Provide them, Please Check"); // Or log and return Optional.empty()
        }

        Set<Long> newRoleIds = new HashSet<>(rolesIds);
        newRoleIds.removeAll(existingRoles.stream().map(Role::getId).toList());

        if (!newRoleIds.isEmpty()) {
            List<Role> newRolesDB = roleMapper.fromDTOs(
                    roleService.getRolesByIDs(new ArrayList<>(newRoleIds)).get()
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

    @Override
    public Optional<ResponseOperation<UserDTO>> getCurrentUser(User user) {
        return
                Optional.of(
                        ResponseOperation.<UserDTO>builder()
                                .message("Current User with essentials Credentials")
                                .object(
                                        UserDTO.builder()
                                                .phone(user.getPhone())
                                                .firstName(user.getFirstName())
                                                .lastName(user.getLastName())
                                                .build()
                                ).build());
    }

    @Override
    public ResponseOperation<?> updateProfile(UpdateProfileRequest request, User user) {

        User userToUpdate = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new EntityNotFoundException("User Not Found"));

        userToUpdate.setLastName(request.getLastName());
        userToUpdate.setFirstName(request.getFirstName());
        userToUpdate.setPhone(request.getPhone());

        userRepository.save(userToUpdate);
        return ResponseOperation.builder()
                .message("User has been updated")
                .build();
    }

    @Override
    public List<DeliveryManDTO> getDeliveries() {
        return userRepository.findAllByRoleLabel(
                        RoleLabel.DELIVERY
        )
                .stream()
                .map(userMapper::toDeliveryMan)
                .toList();
    }

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User Not Found"));
    }
}
