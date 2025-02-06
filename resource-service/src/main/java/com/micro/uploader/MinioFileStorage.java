package com.micro.uploader;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class MinioFileStorage {
    private final MinioClient minioClient;
    private final String bucketName;

    @Autowired
    public MinioFileStorage(MinioClient minioClient, @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public void uploadFile(String fileName, String contentType, byte[] content) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    public byte[] downloadFileAsByteArray(String fileName) {
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error reading file into byte array: " + e.getMessage(), e);
        }
    }

    public void removeFiles(List<String> fileNames) {
        List<DeleteObject> objects = fileNames.stream().map(DeleteObject::new).toList();
        StringBuilder errorBuilder = new StringBuilder();
        try {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                        RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                try {
                    DeleteError error = result.get();
                    errorBuilder.append(String.format("Error in deleting object %s: %s; ",
                        error.objectName(), error.message()));
                } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
                    throw new RuntimeException("Error processing result of deletion", e);
                }
            }
        } catch (MinioException e) {
            throw new RuntimeException("Server-side error occurred while deleting files.", e);
        }

        if (!errorBuilder.isEmpty()) {
            throw new RuntimeException("Failed to delete some objects: " + errorBuilder.toString());
        }
    }
}
