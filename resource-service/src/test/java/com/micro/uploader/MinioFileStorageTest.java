package com.micro.uploader;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MinioFileStorageTest {

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Test
    void uploadFileSuccessfulUpload() throws Exception {
        MinioClient minioClient = mock(MinioClient.class);
        MinioFileStorage minioFileStorage = new MinioFileStorage(minioClient, this.bucketName);
        String fileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, world!".getBytes();

        when(minioClient.bucketExists(Mockito.any())).thenReturn(true);

        minioFileStorage.uploadFile(fileName, contentType, content);

        verify(minioClient).putObject(Mockito.argThat(args ->
        {
            try {
                return args != null && args.object().equals(fileName) &&
                        args.contentType().equals(contentType);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e);
            }
        }));
    }

    @Test
    void uploadFileBucketDoesNotExistCreatesBucketAndUploadsFile() throws Exception {
        MinioClient minioClient = mock(MinioClient.class);
        MinioFileStorage minioFileStorage = new MinioFileStorage(minioClient, this.bucketName);
        String fileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, world!".getBytes();

        when(minioClient.bucketExists(Mockito.any())).thenReturn(false);
        minioFileStorage.uploadFile(fileName, contentType, content);

        verify(minioClient).makeBucket(Mockito.any());
        verify(minioClient).putObject(Mockito.argThat(args ->
        {
            try {
                return args != null && args.object().equals(fileName) && args.contentType().equals(contentType);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e);
            }
        }));
    }

    @Test
    void uploadFileExceptionDuringUploadThrowsRuntimeException() throws Exception {
        MinioClient minioClient = mock(MinioClient.class);
        MinioFileStorage minioFileStorage = new MinioFileStorage(minioClient, this.bucketName);
        String fileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, world!".getBytes();

        when(minioClient.bucketExists(Mockito.any())).thenReturn(true);
        doThrow(new RuntimeException("Mocked runtime error caused by Minio")).when(minioClient)
                .putObject(Mockito.any());
        assertThrows(RuntimeException.class, () -> minioFileStorage.uploadFile(fileName, contentType, content));
    }

    @Test
    void uploadFileIoExceptionDuringByteArrayInputThrowsRuntimeException() throws Exception {
        MinioClient minioClient = mock(MinioClient.class);
        MinioFileStorage minioFileStorage = new MinioFileStorage(minioClient, this.bucketName);
        byte[] content = new byte[]{};

        doThrow(new InvalidKeyException("Invalid Key"))
                .when(minioClient)
                .putObject(Mockito.any(PutObjectArgs.class));

        assertThrows(RuntimeException.class, () -> minioFileStorage.uploadFile("file.txt", "text/plain", content));
    }
}