package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {}
