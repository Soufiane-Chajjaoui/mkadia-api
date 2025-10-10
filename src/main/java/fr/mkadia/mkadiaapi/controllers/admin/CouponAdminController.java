package fr.mkadia.mkadiaapi.controllers.admin;

import fr.mkadia.mkadiaapi.dtos.CouponDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.entities.Coupon;
import fr.mkadia.mkadiaapi.enums.DiscountType;
import fr.mkadia.mkadiaapi.mappers.CouponMapper;
import fr.mkadia.mkadiaapi.repositories.CouponRepository;
import fr.mkadia.mkadiaapi.services.coupon.CouponService;
import fr.mkadia.mkadiaapi.specifications.CouponSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/coupon")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CouponAdminController {
    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    /**
     * 📌 Récupérer un coupon par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Integer id) {
        return couponService.getCouponById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ✍️ Créer un nouveau coupon
     */
    @PostMapping
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO couponDTO) {
        CouponDTO couponSaved = couponService.createCoupon(couponDTO);
        return ResponseEntity.created(
                URI.create(STR."/api/v1/admin/coupons/\{couponSaved.getId()}")
        ).body(couponSaved);
    }

    /**
     * 🔄 Mettre à jour un coupon existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Integer id, @RequestBody CouponDTO couponDTO) {
        return couponRepository.findById(id)
                .map(existing -> {
                    Coupon updated = couponMapper.fromDTO(couponDTO);
                    updated.setId(existing.getId());
                    Coupon saved = couponRepository.save(updated);
                    return ResponseEntity.ok(couponMapper.fromEntity(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🗑 Supprimer un coupon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
        if (couponRepository.existsById(id)) {
            couponRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * filter des coupons
     */
    @GetMapping("/filter")
    public ElementsOfPageDTO<CouponDTO> filterCoupons(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) DiscountType type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false , defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return couponService.filterCoupons(search, type, status, startDate, endDate, page, size);
    }
}
