package com.kodnest.best_shop.request;

import com.kodnest.best_shop.model.Category;
import com.kodnest.best_shop.model.Image;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private  String brand;
    private  String description;
    private BigDecimal price;
    private  int inventory;
    private Category category;
    private List<Image> images;
}
