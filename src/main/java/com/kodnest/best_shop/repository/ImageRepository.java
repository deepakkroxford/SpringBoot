package com.kodnest.best_shop.repository;

import com.kodnest.best_shop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
