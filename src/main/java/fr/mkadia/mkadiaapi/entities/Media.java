package fr.mkadia.mkadiaapi.entities;

import fr.mkadia.mkadiaapi.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Integer id;
    @Column(name = "url" , nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType type;

    @ManyToOne(fetch = FetchType.LAZY , cascade =  CascadeType.ALL)
    @JoinColumn(name = "product_id"
            , nullable = false
            , columnDefinition = "INT"
            , referencedColumnName = "product_id"
            , foreignKey = @ForeignKey(name = "fk_media_product"))
    private Product products;
}
