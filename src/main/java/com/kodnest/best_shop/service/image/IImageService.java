package com.kodnest.best_shop.service.image;

import com.kodnest.best_shop.dto.ImageDto;
import com.kodnest.best_shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId);

}
