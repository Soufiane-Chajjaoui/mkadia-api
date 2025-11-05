package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.mkadia.mkadiaapi.enums.ProductStatus;
import fr.mkadia.mkadiaapi.enums.ProductUnit;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    // ✅ Identifiant (null pour création)
    private Integer id;

    // ✅ Infos de base
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String name;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    private String description;

    // ✅ Références
    @NotBlank(message = "Le SKU est obligatoire")
    private String sku;

    @Pattern(regexp = "^[0-9]{8,13}$", message = "Code-barres invalide")
    private String barcode;

    // ✅ Infos supplémentaires
    private String brand;   // Ex : Danone, Coca-Cola
    private String origin;  // Ex : Maroc, Espagne

    // ✅ Prix & Stock
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    @DecimalMax(value = "100.00", message = "La remise ne peut dépasser 100%")
    private Double discountPercentage;

    @Min(value = 0, message = "Le stock ne peut être négatif")
    private int stock;

    // ✅ Statut
    @NotNull(message = "Le statut est obligatoire")
    private ProductStatus status;

    // ✅ Métadonnées
    @JsonProperty("isNew")
    private boolean newProduct;

    @JsonProperty("isFeatured")
    private boolean featured;

    // ✅ Unité
    @NotNull(message = "L'unité est obligatoire")
    private ProductUnit unit;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Double quantity;

    // ✅ Dates
    @Future(message = "La date d'expiration doit être dans le futur")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    // ✅ SEO
    @Size(max = 200, message = "Le slug ne doit pas dépasser 200 caractères")
    private String slug;

    @Size(max = 255, message = "Le metaTitle ne doit pas dépasser 255 caractères")
    private String metaTitle;

    @Size(max = 500, message = "Le metaDesc ne doit pas dépasser 500 caractères")
    private String metaDesc;

    // ✅ Relations
    @NotNull(message = "La catégorie est obligatoire")
    private CategoryDTO category;

    private List<ReviewDTO> reviews;

    // ✅ Images
    private List<MediaDTO> urls;

    // ✅ Timestamps (en lecture seule)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
