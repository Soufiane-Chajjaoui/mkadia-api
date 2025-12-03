package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
