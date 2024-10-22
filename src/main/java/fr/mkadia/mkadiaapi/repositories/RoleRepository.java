package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findAllByIdIn(List<Integer> IDs);
    Optional<Role> findFirstByLabelContainingOrderByLabelAsc(String label);
    Set<Role> findAllByIsDefaultTrue();

}
