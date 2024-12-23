package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media , Integer> {
}
