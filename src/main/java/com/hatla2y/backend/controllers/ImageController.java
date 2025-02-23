package com.hatla2y.backend.controllers;

import com.hatla2y.backend.dtos.files.FileUploadResponse;
import com.hatla2y.backend.dtos.files.UploadStatus;
import com.hatla2y.backend.service.AzureStorageService;
import com.hatla2y.backend.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/img")
public class ImageController {
    private final ImageService imageService;
    private final AzureStorageService azureStorageService;

    public ImageController(ImageService imageService, AzureStorageService azureStorageService) {
        this.imageService = imageService;
        this.azureStorageService = azureStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadResponse response = azureStorageService.upload(file);
        if (response.getStatus() == UploadStatus.SUCCESS) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
