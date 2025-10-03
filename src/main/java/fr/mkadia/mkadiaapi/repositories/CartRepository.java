package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Cart;
import fr.mkadia.mkadiaapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);
}
