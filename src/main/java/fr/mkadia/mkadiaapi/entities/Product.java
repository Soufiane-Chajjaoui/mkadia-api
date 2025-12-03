package fr.mkadia.mkadiaapi.entities;

import fr.mkadia.mkadiaapi.enums.ProductStatus;
import fr.mkadia.mkadiaapi.enums.ProductUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(columnList = "status"),
                @Index(columnList = "category_id"),
                @Index(columnList = "price")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    // ✅ Identifiant unique
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    // ✅ Nom du produit
    @NotNull
    @Size(max = 150)
    @Column(nullable = false)
    private String name;

    // ✅ Description courte ou détaillée
    @Size(max = 1000)
    private String description;

    // ✅ SKU - Identifiant interne unique
    @Column(unique = true, nullable = false)
    private String sku;

    // ✅ Barcode - pour scan rapide
    @Column(unique = true)
    private String barcode;

    // ✅ Infos supplémentaires
    private String brand;   // Ex : Danone, Coca-Cola
    private String origin;  // Ex : Maroc, Espagne

    // ✅ Prix du produit
    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // ✅ Champs pour promotions
    private Double discountPercentage; // Ex: 10.0

    // ✅ Stock disponible
    private int stock;

    // ✅ Statut : ACTIVE, INACTIVE, DRAFT
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    // ✅ Mise en avant
    @Column(name = "is_new")
    private boolean newProduct;
    @Column(name = "is_featured")
    private boolean featured;

    // ✅ Unité et quantité standard (ex : 1.0 kg, 0.5 L)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductUnit unit;

    @Column(nullable = false)
    private Double quantity;

    private LocalDate expirationDate;

    // ✅ SEO (utile pour site web)
    private String slug;
    private String metaTitle;
    private String metaDesc;

    // ✅ Relation avec la catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
            referencedColumnName = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;

    // ✅ Liste des images du produit
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Media> urls = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
