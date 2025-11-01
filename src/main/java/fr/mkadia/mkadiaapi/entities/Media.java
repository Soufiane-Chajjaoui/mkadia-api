package fr.mkadia.mkadiaapi.entities;

import fr.mkadia.mkadiaapi.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Integer id;
    @Column(name = "url" , nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", columnDefinition = "type_of_media" , nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class) // to fix cast expression varying compatible with PostgreSQL
    private MediaType type;

    @ManyToOne(fetch = FetchType.LAZY , cascade =  CascadeType.ALL)
    @JoinColumn(name = "product_id"
            , nullable = false
            , columnDefinition = "INT"
            , referencedColumnName = "product_id"
            , foreignKey = @ForeignKey(name = "fk_media_product"))
    private Product product;
    private Integer position;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
