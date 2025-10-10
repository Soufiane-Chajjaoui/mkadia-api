package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.CouponDTO;
import fr.mkadia.mkadiaapi.entities.Coupon;
import fr.mkadia.mkadiaapi.enums.DiscountType;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.InvalidCouponException;
import fr.mkadia.mkadiaapi.mappers.CouponMapper;
import fr.mkadia.mkadiaapi.models.ApplyCouponRequest;
import fr.mkadia.mkadiaapi.models.CouponResponse;
import fr.mkadia.mkadiaapi.repositories.CouponRepository;
import fr.mkadia.mkadiaapi.services.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponUserController {

    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @PostMapping("/apply")
    public ResponseEntity<CouponResponse> applyCoupon(@Valid @RequestBody ApplyCouponRequest request) {
        try {
            // Vérifier la validité du coupon
            Coupon coupon = couponService.verifyCouponByCode(request.getCode());

            // Calculer la réduction
            BigDecimal discount = couponService.calculateDiscount(coupon, request.getCartAmount());

            // Construire la réponse
            CouponResponse response = CouponResponse.builder()
                    .code(coupon.getCode())
                    .discountType(coupon.getDiscountType())
                    .discountValue(discount)
                    .discountPercentage(coupon.getDiscountType() == DiscountType.PERCENTAGE
                            ? coupon.getDiscountValue()
                            : null)
                    .message("Code promo appliqué avec succès")
                    .build();

            return ResponseEntity.ok(response);

        } catch (InvalidCouponException e) {
            throw e; // Sera géré par le GlobalExceptionHandler
        } catch (Exception e) {
            throw new InvalidCouponException("Erreur lors de l'application du code promo");
        }
    }

    /**
     * Vérifier si un code promo existe et est valide (sans l'appliquer)
     */
    @GetMapping("/validate/{code}")
    public ResponseEntity<CouponResponse> validateCoupon(
            @PathVariable String code,
            @RequestParam BigDecimal cartAmount) {

        Coupon coupon = couponService.verifyCouponByCode(code);
        BigDecimal discount = couponService.calculateDiscount(coupon, cartAmount);

        CouponResponse response = CouponResponse.builder()
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(discount)
                .discountPercentage(coupon.getDiscountType() == DiscountType.PERCENTAGE
                        ? coupon.getDiscountValue()
                        : null)
                .message("Code promo valide")
                .build();

        return ResponseEntity.ok(response);
    }
}
