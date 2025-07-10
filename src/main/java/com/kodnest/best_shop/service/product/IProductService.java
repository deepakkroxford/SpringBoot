package com.kodnest.best_shop.service.product;

import com.kodnest.best_shop.dto.ProductDto;
import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.request.AddProductRequest;
import com.kodnest.best_shop.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
   Product addProduct(AddProductRequest request);
   Product getProductById(Long Id);
   void deleteProductById(Long Id);
   Product updateProduct(ProductUpdateRequest request, Long productId);
   List<Product> getAllProducts();
   List<Product> getProductByCategory(String category);
   List<Product>getProductByBrand(String brand);
   List<Product> getProductByCategoryAndBrand(String category, String brand);
   List<Product> getProductByName(String productName);
   List<Product> getProductByBrandAndName(String brand, String name);
   Long countProductsByBrandAndName(String brand, String name);

   List<ProductDto> getConvertProducts(List<Product> products);

   ProductDto convertToDto(Product product);
}
