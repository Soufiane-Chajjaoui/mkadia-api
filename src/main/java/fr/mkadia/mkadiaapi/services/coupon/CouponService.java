package fr.mkadia.mkadiaapi.services.coupon;

import fr.mkadia.mkadiaapi.dtos.CategoryDTO;
import fr.mkadia.mkadiaapi.dtos.CouponDTO;
import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.entities.Coupon;
import fr.mkadia.mkadiaapi.enums.DiscountType;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.InvalidCouponException;
import fr.mkadia.mkadiaapi.mappers.CouponMapper;
import fr.mkadia.mkadiaapi.repositories.CouponRepository;
import fr.mkadia.mkadiaapi.specifications.CouponSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    /**
     * Récupère un coupon par ID
     */
    public Optional<CouponDTO> getCouponById(Integer id) {
        return Optional.of(
                couponMapper.fromEntity(
                        couponRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"))
                )
        );
    }
    /**
     * Vérifier la validité d'un coupon
     */
    public Coupon verifyCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new InvalidCouponException("Code promo invalide"));

        // Vérifier si actif
        if (!coupon.isActive()) {
            throw new InvalidCouponException("Ce code promo n'est plus actif");
        }

        // Vérifier les dates
        LocalDate now = LocalDate.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
            throw new InvalidCouponException("Ce code promo n'est pas encore valide");
        }
        if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
            throw new InvalidCouponException("Ce code promo a expiré");
        }

        // Vérifier la limite d'utilisation
        if (coupon.getUsageLimit() != null &&
                coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new InvalidCouponException("Ce code promo a atteint sa limite d'utilisation");
        }

        return coupon;
    }

    /**
     * Calculer la réduction
     */
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal cartAmount) {
        // Vérifier le montant minimum
        if (coupon.getMinOrderAmount() != null &&
                cartAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new InvalidCouponException(
                    String.format("Montant minimum requis: %.2f€", coupon.getMinOrderAmount())
            );
        }

        BigDecimal discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            // Réduction en pourcentage
            discount = cartAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // Appliquer la réduction maximale si définie
            if (coupon.getMaxDiscountAmount() != null &&
                    discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else {
            // Réduction fixe
            discount = coupon.getDiscountValue();

            // S'assurer que la réduction ne dépasse pas le montant du panier
            if (discount.compareTo(cartAmount) > 0) {
                discount = cartAmount;
            }
        }

        return discount;
    }

    /**
     * Obtenir un coupon par son code
     */
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Coupon introuvable"));
    }

    /**
     * Incrémenter le compteur d'utilisation lors de la commande
     */
    @Transactional
    public void incrementUsageCount(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Code promo introuvable"));

        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
    }

    public CouponDTO createCoupon(CouponDTO couponDTO) {


        Coupon coupon = couponMapper.fromDTO(couponDTO);
        if (DiscountType.FREE_DELIVERY == couponDTO.getDiscountType()) {
            coupon.setDiscountValue(null);
        }
        return couponMapper.fromEntity(couponRepository.save(coupon));
    }

    public ElementsOfPageDTO<CouponDTO> filterCoupons(String search,
                                                      DiscountType type,
                                                      String status,
                                                      LocalDate startDate,
                                                      LocalDate endDate,
                                                      int page,
                                                      int size) {

        Specification<Coupon> spec = Specification
                .where(CouponSpecifications.hasCodeContaining(search))
                .and(CouponSpecifications.hasType(type))
                .and(CouponSpecifications.hasStatus(status))
                .and(CouponSpecifications.startDateAfter(startDate))
                .and(CouponSpecifications.endDateBefore(endDate));

        Pageable pageable = PageRequest.of(page, size);
        Page<Coupon> pageOfCoupons = couponRepository.findAll(spec, pageable);

        List<CouponDTO> coupons = pageOfCoupons.getContent()
                .stream()
                .map(couponMapper::fromEntity)
                .collect(Collectors.toList());

        ElementsOfPageDTO<CouponDTO> result = ElementsOfPageDTO.<CouponDTO>builder()
                .totalPages(pageOfCoupons.getTotalPages())
                .pageSize(pageOfCoupons.getSize())
                .totalRecords(pageOfCoupons.getTotalElements())
                .currentPage(page)
                .elementsDTO(coupons)
                .build();

        return result;
    }

}
