package fr.mkadia.mkadiaapi.controllers;

import fr.mkadia.mkadiaapi.services.file.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MinioController {

    private final MinioStorageService minioStorageService;

    @PostMapping(value = "/upload" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){

        try {
//            String fileName = minioStorageService.uploadFile(file);
            return ResponseEntity.ok(STR."Fichier ete bien uploader " );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(STR."Erreur d'upload : \{e.getMessage()}");
        }
    }
}
