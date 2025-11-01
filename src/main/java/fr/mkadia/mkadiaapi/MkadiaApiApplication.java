package fr.mkadia.mkadiaapi;

import fr.mkadia.mkadiaapi.dtos.UserDTO;
import fr.mkadia.mkadiaapi.entities.Role;
import fr.mkadia.mkadiaapi.enums.RoleLabel;
import fr.mkadia.mkadiaapi.repositories.RoleRepository;
import fr.mkadia.mkadiaapi.services.authentication.AuthService;
import fr.mkadia.mkadiaapi.services.authorization.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MkadiaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MkadiaApiApplication.class, args);
    }


    @Bean
    public CommandLineRunner lineRunner(RoleRepository roleRepository, AuthService authService, RoleService roleService)
    {
        return args -> {
            if (roleRepository.findAll().isEmpty()){
                roleRepository.save(Role.builder().label(RoleLabel.ADMIN).isDefault(true).build());
                roleRepository.save(Role.builder().label(RoleLabel.DELIVERY).isDefault(true).build());
                roleRepository.save(Role.builder().label(RoleLabel.USER).isDefault(true).build());
                authService.registerUser(
                        UserDTO.builder()
                                .email("schajjaoui2003@gmail.com")
                                .phone("+212607025329")
                                .password("Soufianch@2211")
                                .lastName("chajjaoui")
                                .firstName("soufiane")
                                .roles(roleService.getDefaultRoles().get()).build()
                );
            }

        };
    }

}
