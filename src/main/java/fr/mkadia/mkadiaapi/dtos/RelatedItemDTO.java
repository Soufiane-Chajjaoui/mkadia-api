package fr.mkadia.mkadiaapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object for creating, updating, and viewing static RelatedItem relationships.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedItemDTO {

    // ID of the relationship itself (used for deletion/updates)
    private Integer id;

    // ✅ ID du produit source (obligatoire pour créer une relation)
    @NotNull(message = "L'ID du produit source est obligatoire")
    private Integer sourceProductId;

    // ✅ ID du produit lié (obligatoire pour créer une relation)
    @NotNull(message = "L'ID du produit lié est obligatoire")
    private Integer relatedProductId;

    // ✅ Type de relation (Optionnel)
    private String relationshipType;

    // --- DTOs for Read Operations (Optional, to simplify client-side data) ---

    // DTO of the related product itself, populated on read
    private ProductDTO relatedProduct;
}