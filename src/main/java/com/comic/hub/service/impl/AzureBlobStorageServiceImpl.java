package com.comic.hub.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.comic.hub.service.AzureBlobStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {

    private final BlobContainerClient blobContainerClient;
    private final String containerName;

    public AzureBlobStorageServiceImpl(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName) {
        this.containerName = containerName;
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        this.blobContainerClient.createIfNotExists();
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String blobName = buildBlobName(file.getOriginalFilename());
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
            String contentType = file.getContentType() == null || file.getContentType().isBlank()
                    ? "application/octet-stream"
                    : file.getContentType();
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo de portada", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("No se pudo subir la portada a Azure Blob Storage", e);
        }
    }

    @Override
    public void deleteByUrl(String blobUrl) {
        if (blobUrl == null || blobUrl.isBlank()) {
            return;
        }

        String blobName = extractBlobName(blobUrl);
        if (blobName == null || blobName.isBlank()) {
            return;
        }

        blobContainerClient.getBlobClient(blobName).deleteIfExists();
    }

    private String buildBlobName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }

    private String extractBlobName(String blobUrl) {
        try {
            URI uri = URI.create(blobUrl);
            String path = uri.getPath();
            String prefix = "/" + containerName + "/";
            if (!path.startsWith(prefix)) {
                return null;
            }
            String encodedBlobName = path.substring(prefix.length());
            return URLDecoder.decode(encodedBlobName, StandardCharsets.UTF_8);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
