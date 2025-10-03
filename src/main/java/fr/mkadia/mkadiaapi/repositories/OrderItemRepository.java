package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Order;
import fr.mkadia.mkadiaapi.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrder(Order order);
}
