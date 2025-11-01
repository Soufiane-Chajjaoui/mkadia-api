package fr.mkadia.mkadiaapi.specifications;

import fr.mkadia.mkadiaapi.entities.Order;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> filterOrders(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate createdAfter,
            LocalDate createdBefore,
            String status,
            String paymentStatus,
            String paymentMethod,
            String search
    ) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par montant
            if (minAmount != null || maxAmount != null) {
                if (minAmount != null && maxAmount != null) {
                    predicates.add(cb.between(root.get("totalAmount"), minAmount, maxAmount));
                } else if (minAmount != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), minAmount));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), maxAmount));
                }
            }

            // Filtre par date
            if (createdAfter != null || createdBefore != null) {
                LocalDateTime start = (createdAfter != null) ? createdAfter.atStartOfDay() : LocalDateTime.MIN;
                LocalDateTime end = (createdBefore != null) ? createdBefore.atTime(23, 59, 59) : LocalDateTime.MAX;
                predicates.add(cb.between(root.get("createdAt"), start, end));
            }

            // Filtre par statut de commande
            if (status != null && !status.trim().isEmpty()) {
                try {
                    OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), orderStatus));
                } catch (IllegalArgumentException ignored) {
                    // Statut invalide ignoré
                }
            }

            // Filtres sur le paiement
            if ((paymentStatus != null && !paymentStatus.trim().isEmpty()) ||
                    (paymentMethod != null && !paymentMethod.trim().isEmpty())) {

                Join<Object, Object> paymentJoin = root.join("payment", JoinType.LEFT);

                if (paymentStatus != null && !paymentStatus.trim().isEmpty()) {
                    predicates.add(cb.equal(
                            cb.lower(paymentJoin.get("status")),
                            paymentStatus.toLowerCase().trim()
                    ));
                }

                if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
                    predicates.add(cb.equal(
                            cb.lower(paymentJoin.get("method")),
                            paymentMethod.toLowerCase().trim()
                    ));
                }
            }

            // 🔍 Filtre par client OU ID de commande
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase().trim() + "%";

                Join<Object, Object> clientJoin = root.join("user", JoinType.LEFT);

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(cb.like(cb.lower(clientJoin.get("firstName")), pattern));
                searchPredicates.add(cb.like(cb.lower(clientJoin.get("lastName")), pattern));
                searchPredicates.add(cb.like(cb.lower(clientJoin.get("email")), pattern));

                // Si le search est un nombre → chercher aussi par ID
                try {
                    Integer id = Integer.parseInt(search.trim());
                    searchPredicates.add(cb.equal(root.get("id"), id));
                } catch (NumberFormatException ignored) {
                    // Si ce n’est pas un nombre, on ignore cette partie
                }

                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
