package com.kodnest.best_shop.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String imagePath;
    private String downloadUrl;
}
