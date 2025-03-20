package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
