package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findAllByIdIn(List<Integer> IDs);
    Optional<Role> findFirstByLabelContainingOrderByLabelAsc(String label);
    Set<Role> findAllByIsDefaultTrue();
}
