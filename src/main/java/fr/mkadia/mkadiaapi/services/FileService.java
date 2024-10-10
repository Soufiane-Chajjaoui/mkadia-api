package fr.mkadia.mkadiaapi.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileService {

    public Optional<String> uploadFile(MultipartFile file, String suffix) throws IOException {
        Path path = Paths.get("D:\\Projets-Prof\\mkadia\\mkadia-app-files", "categories");

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String fileName = STR."category-\{suffix}";
        Path filePath = path.resolve(STR."\{fileName}.pdf");

        Files.copy(file.getInputStream(), filePath , StandardCopyOption.REPLACE_EXISTING);

        return Optional.of(filePath.toUri().toString());
    }
}
