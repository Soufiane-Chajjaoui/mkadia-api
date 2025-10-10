package fr.mkadia.mkadiaapi.repositories;

import fr.mkadia.mkadiaapi.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer>, JpaSpecificationExecutor<Coupon> {
    Optional<Coupon> findByCode(String code);
}
