package fr.mkadia.mkadiaapi.specifications;

import fr.mkadia.mkadiaapi.entities.Coupon;
import fr.mkadia.mkadiaapi.enums.DiscountType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CouponSpecifications {

    public static Specification<Coupon> hasCodeContaining(String search) {
        return (root, query, cb) ->
                (search == null || search.isEmpty())
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("code")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<Coupon> hasType(DiscountType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("discountType"), type);  // ✅ No casting needed
        };
    }

    public static Specification<Coupon> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction();
            }
            boolean isActive = status.equalsIgnoreCase("active");
            return cb.equal(root.get("active"), isActive);
        };
    }

    public static Specification<Coupon> startDateAfter(LocalDate startDate) {
        return (root, query, cb) ->
                (startDate == null)
                        ? cb.conjunction()
                        : cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Coupon> endDateBefore(LocalDate endDate) {
        return (root, query, cb) ->
                (endDate == null)
                        ? cb.conjunction()
                        : cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }
}