package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Page<Favorite> findByUserId(Integer userId, Pageable pageable);
}
