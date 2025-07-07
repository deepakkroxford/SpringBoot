package com.kodnest.best_shop.controller;


import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.request.AddProductRequest;
import com.kodnest.best_shop.response.ApiResponse;
import com.kodnest.best_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product")
public class ProductController {
    private final IProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
           Product newProduct = productService.addProduct(product);
           return ResponseEntity.ok().body(new ApiResponse("success",newProduct));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed TO ADD THE PRODUCT!",null));
        }
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductId(@PathVariable Long id) {
        try {
            Product product  = productService.getProductById(id);
            return ResponseEntity.ok().body(new ApiResponse("success",product));
        }catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed",null));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        try {
            List<Product> allProduct = productService.getAllProducts();
            return ResponseEntity.ok().body(new ApiResponse("success",allProduct));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed",null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok().body(new ApiResponse("success",null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed",null));
        }
    }

}



