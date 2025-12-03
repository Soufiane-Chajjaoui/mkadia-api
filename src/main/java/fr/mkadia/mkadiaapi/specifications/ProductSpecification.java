package fr.mkadia.mkadiaapi.specifications;

import fr.mkadia.mkadiaapi.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filterProducts(
            String search,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String status,
            String stockStatus,
            LocalDate createdAfter,
            LocalDate createdBefore
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Recherche par nom, description, SKU, barcode
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = STR."%\{search.toLowerCase()}%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), searchPattern
                );
                Predicate descPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), searchPattern
                );
                Predicate skuPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("sku")), searchPattern
                );
                Predicate barcodePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("barcode")), searchPattern
                );

                predicates.add(criteriaBuilder.or(
                        namePredicate, descPredicate, skuPredicate, barcodePredicate
                ));
            }

            // Filtre par catégorie
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("category").get("id"), categoryId
                ));
            }

            // Filtre par prix minimum
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice
                ));
            }

            // Filtre par prix maximum
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice
                ));
            }

            // Filtre par statut
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("status"), status
                ));
            }

            // Filtre par état du stock
            if (stockStatus != null && !stockStatus.isEmpty()) {
                switch (stockStatus) {
                    case "IN_STOCK":
                        predicates.add(criteriaBuilder.greaterThan(root.get("stock"), 10));
                        break;
                    case "LOW_STOCK":
                        predicates.add(criteriaBuilder.between(root.get("stock"), 1, 10));
                        break;
                    case "OUT_OF_STOCK":
                        predicates.add(criteriaBuilder.equal(root.get("stock"), 0));
                        break;
                }
            }

            // Filtre par date de création (après)
            if (createdAfter != null) {
                LocalDateTime startOfDay = createdAfter.atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), startOfDay
                ));
            }

            // Filtre par date de création (avant)
            if (createdBefore != null) {
                LocalDateTime endOfDay = createdBefore.atTime(23, 59, 59);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"), endOfDay
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}