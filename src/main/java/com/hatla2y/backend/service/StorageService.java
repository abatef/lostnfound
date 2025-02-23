package com.hatla2y.backend.service;

import com.hatla2y.backend.dtos.files.FileUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {
    FileUploadResponse upload(MultipartFile file) throws IOException;
}
