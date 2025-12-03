package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.RoleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.label = :role")
    List<User> findAllByRoleLabel(@Param("role") RoleLabel role);
}

