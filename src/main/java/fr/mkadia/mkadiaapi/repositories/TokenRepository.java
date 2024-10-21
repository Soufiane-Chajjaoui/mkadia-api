package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token , Long> {

    Optional<Token> findByToken(String token);

    @Query("SELECT t from Token t WHERE t.user.id = :id and (t.expired = false or t.revoked = false) AND t.token <> :currentRefreshToken")
    List<Token> findAllValidTokenByUser(@Param("id") Long id, @Param("currentRefreshToken") String currentRefreshToken);
}
