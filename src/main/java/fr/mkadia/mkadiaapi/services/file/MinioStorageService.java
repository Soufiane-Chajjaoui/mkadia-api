package fr.mkadia.mkadiaapi.services.file;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MinioStorageService {

    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String COMMON_BUCKET_NAME;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.put-object-part-size}")
    private Long putObjectPartSize;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public Optional<String> uploadObject(MultipartFile file) throws IOException   {

        String fileName = STR."\{UUID.randomUUID()}.\{this.extractExtension(file.getOriginalFilename())}";
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.COMMON_BUCKET_NAME)
                            .object(fileName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), putObjectPartSize)
                            .build()
            );
            return Optional.of(this.getFileUrl(fileName));
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> uploadFileSync(String fileName, InputStream inputStream, String contentType){
        String newFileName = STR."\{UUID.randomUUID()}_\{fileName}";
        return CompletableFuture.supplyAsync(()-> {
            try {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(COMMON_BUCKET_NAME)
                                .object(newFileName)
                                .stream(inputStream, -1, putObjectPartSize)
                                .contentType(contentType)
                                .build()
                );
                return this.getFileUrl(newFileName);
            } catch (Exception e) {
                throw new RuntimeException(STR."Erreur lors de l'upload du fichier : \{fileName}", e);
            }

        });
    }

    public List<String> uploadMultipleFiles(List<MultipartFile> files){

        List<CompletableFuture<String>> futures = this.fileUploadRequests(files)
                .stream()
                .map(file-> uploadFileSync(file.getFileName(), file.getInputStream(), file.getContentType()))
                .toList();
        return futures
                .stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private String getFileUrl(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return extractPublicFileUrl(minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .bucket(this.COMMON_BUCKET_NAME)
                        .object(fileName)
                        .method(Method.GET)
                        .build()
        ));
    }

    public Optional<Boolean> deleteObject(String urlObject){
        try {
            String objectName = extractObjectName(urlObject);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(this.COMMON_BUCKET_NAME)
                            .object(objectName)
                            .build()
            );
            return Optional.of(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream(UUID uuid, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(this.COMMON_BUCKET_NAME)
                        .offset(offset)
                        .length(length)
                        .object(uuid.toString())
                        .build());
    }

    public boolean objectExist(String objectUrl){
        if (!objectUrl.isEmpty()){
            return false;
        }
        try {
            String objectName = extractObjectName(objectUrl);
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(this.COMMON_BUCKET_NAME).object(objectName).build()
            );
            return false;
        } catch (ErrorResponseException e) {
            if (e.response().code() == 404){
                return false;
            }
            throw new RuntimeException("Error checking existence in Minio" , e);
        } catch (Exception e){
            throw new RuntimeException("Error checking object existence in MinIO", e);
        }
    }

    public String extractObjectName(String objectUrl){
        return objectUrl.substring(objectUrl.lastIndexOf("/") + 1);
    }

    public boolean isSameObject(String oldObjectName, MultipartFile file) {
        try {
            StatObjectResponse statObjectArgs = minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(COMMON_BUCKET_NAME)
                            .object(oldObjectName).build()
            );
            return statObjectArgs.size() == file.getSize();
        } catch (Exception e) {
            return false;
        }
    }

    public String extractPublicFileUrl(String objectName){
        return objectName.split("\\?")[0];
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // Retourne une chaîne vide si l'extension n'existe pas
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private List<FileUploadRequest> fileUploadRequests(List<MultipartFile> files){

        return files
                .stream()
                .map(file -> {
                    try {
                        return new MinioStorageService.FileUploadRequest(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }


    @Data
    @AllArgsConstructor @NoArgsConstructor
    public static class FileUploadRequest {
        private String fileName;
        private String contentType;
        private InputStream inputStream;
    }



}
