package com.kodnest.best_shop.service.image;

import com.kodnest.best_shop.dto.ImageDto;
import com.kodnest.best_shop.exceptions.ImageNotFound;
import com.kodnest.best_shop.model.Image;
import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.repository.ImageRepository;
import com.kodnest.best_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        try {
            return imageRepository.findById(id).orElseThrow(()-> new ImageNotFound("Image not found with id" + id));
        } catch (ImageNotFound e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImageById(Long id) {
            imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()-> {
                try {
                    throw new ImageNotFound("No image found with id:" + id);
                } catch (ImageNotFound e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile file:files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);


                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                imageRepository.save(image);
                Image saveImage = imageRepository.save(image);
                saveImage.setDownloadUrl(buildDownloadUrl + saveImage.getId());
                imageRepository.save(saveImage);


                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(saveImage.getId());
                imageDto.setImageName(saveImage.getFileName());
                imageDto.setDownloadUrl(saveImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch ( Exception e) {
                throw new RuntimeException(e);
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
        image.setFileName(file.getOriginalFilename());
        image.setImage(new SerialBlob(file.getBytes()));
        imageRepository.save(image);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        } catch (SerialException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
