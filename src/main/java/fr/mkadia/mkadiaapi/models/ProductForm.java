package fr.mkadia.mkadiaapi.models;

import fr.mkadia.mkadiaapi.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductForm {
    private ProductDTO product;
    private List<MultipartFile> files;
}
