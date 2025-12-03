package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Media;
import fr.mkadia.mkadiaapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media , Long> {
    List<Media> findAllByProduct(Product product);
}
