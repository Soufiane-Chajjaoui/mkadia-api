package fr.mkadia.mkadiaapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;

    @Column(name = "price" , nullable = false)
    private BigDecimal price;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id"
            , nullable = false
            , columnDefinition = "category_id"
            , referencedColumnName = "category_id"
            , foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @OneToMany(mappedBy = "products"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.ALL)
    private List<Media> urls = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
