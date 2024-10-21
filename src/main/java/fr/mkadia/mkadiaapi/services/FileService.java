package fr.mkadia.mkadiaapi.services;

import fr.mkadia.mkadiaapi.exceptions.UnsupportedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Optional<String> saveFile(MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new UnsupportedException("Only JPEG or PNG images are allowed");
        }
        Path pathDir = Paths.get(this.uploadDir);

        if (!Files.exists(pathDir)) {
            Files.createDirectories(pathDir);
        }

        String extension = contentType.split("/")[1];

        String fileName = STR."\{new Date().getTime()}.\{extension}";
        Path filePath = pathDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath , StandardCopyOption.REPLACE_EXISTING);
        return Optional.of(filePath.toUri().toString());
    }

    public void deleteFile(String url) throws IOException {
        Path path = Paths.get(url);
        Files.deleteIfExists(path);
    }

    public boolean fileExist(String url) {
        Path path = Paths.get(url);
        return Files.exists(path);
    }
}
