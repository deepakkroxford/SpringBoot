package com.kodnest.best_shop.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
