package fr.mkadia.mkadiaapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categories")
@AllArgsConstructor @NoArgsConstructor @Builder @Getter @Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    @Column(name = "url")
    private String url;
    @OneToMany(fetch = FetchType.LAZY
            , cascade = CascadeType.ALL
            , mappedBy = "category"
            , orphanRemoval = true)
    private Set<Product> products;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
