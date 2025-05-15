package fr.mkadia.mkadiaapi.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private Bucket bucket ;

    private String accessKey;

    private String secretKey;

    private String url;


    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket.name).build()))
        {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucket.name).build()
            );
        }
        return minioClient;
    }

    @Data
    public static class Bucket {
        private String name;
    }

}
