package com.hatla2y.backend.service;

import com.hatla2y.backend.models.Image;
import com.hatla2y.backend.models.Item;
import com.hatla2y.backend.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Image create(String url, Item item) {
        Image image = new Image();
        image.setUrl(url);
        image.setItem(item);
        return imageRepository.save(image);
    }

    @Transactional
    public void deleteItemImage(Integer imageId) {
        imageRepository.deleteById(imageId);
    }
}
