package com.kodnest.best_shop.controller;

import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.repository.ImageRepository;
import com.kodnest.best_shop.request.AddProductRequest;
import com.kodnest.best_shop.request.ProductUpdateRequest;
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
            return ResponseEntity.ok().body(new ApiResponse("success", newProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("failed TO ADD THE PRODUCT!", null));
        }
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductId(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok().body(new ApiResponse("success", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed", null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        try {
            List<Product> allProduct = productService.getAllProducts();
            return ResponseEntity.ok().body(new ApiResponse("success", allProduct));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed", null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok().body(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("failed", null));
        }
    }

    @GetMapping("/product/{name}/product")
    public ResponseEntity<ApiResponse> findProductByName(@PathVariable String name) {
        try {
            List<Product> productByName = productService.getProductByName(name);
            if (productByName.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no data is found", null));
            } else {
                return ResponseEntity.ok().body(new ApiResponse("success", productByName));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed", null));
        }
    }

    @PostMapping("/product/{productiId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest newData, @PathVariable Long id) {
        try {
            Product updatedData = productService.updateProduct(newData, id);
            return ResponseEntity.ok().body(new ApiResponse("updated Succesfully", updatedData));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed", null));
        }
    }

    /**
     * addProduct Done
     * getProductById
     * getAllProduct
     * deleteById
     * findByproductName - Done
     * updateProduct - Done
     * getProductByCategory - Done
     * getProductByCategoryAndBrand - Done
     * getProductByBrand - Done
     * getProductByBrandAndName - Done
     */

    @GetMapping("/product/{category}/product")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<Product> productList = productService.getProductByCategory(category);
            if (productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no productByCategory Fount", null));
            } else {
                return ResponseEntity.ok().body(new ApiResponse("all data fetched successfully", productList));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed", null));
        }
    }

    @GetMapping("/product/category-and-brand/product")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String category,
            @PathVariable String brand) {
        try {
            List<Product> productListOfCategoryAndBrand = productService.getProductByCategoryAndBrand(category, brand);
            if (productListOfCategoryAndBrand.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("no data is present for CategoryAndProduct", null));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResponse("all data fetched successfully", productListOfCategoryAndBrand));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed", null));
        }
    }

    @GetMapping("/product/{brandName}/product")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand) {
        try {
            List<Product> listOfBrand = productService.getProductByBrand(brand);
            if (listOfBrand.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("getProductByBrand is empty ", null));
            } else {
                return ResponseEntity.ok().body(new ApiResponse("succesfully get the list of brand", listOfBrand));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed", null));
        }
    }

    @GetMapping("/product/{brand}/{name}/product")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String brand, @PathVariable String name) {

        try {
            List<Product> allData = productService.getProductByBrandAndName(brand, name);
            if (allData.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("data is empty for getProductByBrandAndName", null));
            } else {
                return ResponseEntity.ok().body(new ApiResponse("Fetched data succesfully", allData));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("not found failed", null));
        }
    }

    @GetMapping("/product/brand/{brand}/name/{name}/count")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@PathVariable String brand,
            @PathVariable String name) {
        try {
            Long count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok().body(new ApiResponse("fethced data successfully", count));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed to fetch", null));
        }
    }

}
