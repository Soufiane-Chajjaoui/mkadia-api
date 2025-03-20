package fr.mkadia.mkadiaapi.services.file;

import fr.mkadia.mkadiaapi.config.FileConfig;
import fr.mkadia.mkadiaapi.exceptions.UnsupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileConfig fileConfig;

    public Optional<String> saveFile(MultipartFile file) {

        String contentType = file.getContentType();
        log.info(contentType);
        if (!fileConfig.getAllowedExtensions().contains(contentType)) {
            throw new UnsupportedException(STR."Only allowed file types are accepted: \{this.fileConfig.getAllowedExtensions().stream().map(s -> s.split("/")[1]).toList()}");
        }
        Path pathDir = Paths.get(this.fileConfig.getUploadDir());

        if (!Files.exists(pathDir)) {
            try {
                Files.createDirectories(pathDir);
            } catch (IOException e) {
                throw new UnsupportedException(e);
            }
        }

        String extension = contentType.split("/")[1];

        String fileName = STR."\{new Date().getTime()}.\{extension}";
        Path filePath = pathDir.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnsupportedException(e);
        }
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
