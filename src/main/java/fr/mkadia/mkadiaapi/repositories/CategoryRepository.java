package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {

    Page<Category> findByNameContainingIgnoreCase(String keyword , Pageable pageable);

    List<Category> findByNameContainingIgnoreCase(String name);
}
