package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String keyword , Pageable pageable);

//    // stick with native SQL and cast
//    @Query(value = "SELECT * FROM products p WHERE p.status = CAST(:status AS product_status) AND p.stock > :stock AND p.is_featured = true ORDER BY p.created_at DESC LIMIT 10", nativeQuery = true)
//    List<Product> findTop10FeaturedWithCast(@Param("status") String status, @Param("stock") int stock);

    @Query(value = "SELECT * FROM products p WHERE p.status = CAST(:status AS product_status) AND p.stock > :stock AND p.is_featured = true ORDER BY p.created_at DESC",
            nativeQuery = true)
    Page<Product> findFeaturedProductsWithPagination(@Param("status") String status,
                                                     @Param("stock") int stock,
                                                     Pageable pageable);


}
