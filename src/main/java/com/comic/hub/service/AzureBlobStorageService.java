package com.comic.hub.service;

import org.springframework.web.multipart.MultipartFile;

public interface AzureBlobStorageService {

    String uploadImage(MultipartFile file);

    void deleteByUrl(String blobUrl);
}
