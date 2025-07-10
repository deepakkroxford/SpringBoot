package com.kodnest.best_shop.service.product;

import com.kodnest.best_shop.dto.ImageDto;
import com.kodnest.best_shop.dto.ProductDto;
import com.kodnest.best_shop.exceptions.ProductNotFoundException;
import com.kodnest.best_shop.model.Category;
import com.kodnest.best_shop.model.Image;
import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.repository.CategoryRepository;
import com.kodnest.best_shop.repository.ImageRepository;
import com.kodnest.best_shop.repository.ProductRepository;
import com.kodnest.best_shop.request.AddProductRequest;
import com.kodnest.best_shop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    // private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        //check if category is found in the DB
        //If yes set it as the new products category
        // If no save it as new Category
        // The set as the new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->
                {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }

    @Override
    public Product getProductById(Long Id) {
        try {
            return productRepository.findById(Id).
                    orElseThrow(()-> new ProductNotFoundException("Product not found"));
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProductById(Long Id) {
        productRepository.findById(Id).ifPresentOrElse(productRepository::delete,
                ()->{
                    try {
                        throw new ProductNotFoundException("Product Not Found");
                    } catch (ProductNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository
                .findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()-> new ProviderNotFoundException("Product not found!!"));
    }


    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

     /**
     * Here we are using the model mapper -> So the model mapper is a java library
     * that help us to map one object into the other object. give give
     * 
     * Imagine you have two class Product and ProductDTO
     * 
     * class Product {
     *     private Long id;
     *     private String name;
     *     private double price;
     *     // getters and setters
     * }
     * 
     * class ProductDto {
     *  private Long id;
     * private String name;
     * private double price;
     * // getters and setters
     * }
     * 
     * Instead of manually copying each field like this:
     * 
     * ProductDto dto = new ProductDto();
     * dto.setId(product.getId());
     * dto.setName(product.getName());
     * dto.setPrice(product.getPrice());
     * 
     * 
     * You can use ModelMapper to do it in one line:
     * ProductDto dto = modelMapper.map(product, ProductDto.class);
     * 
     * 
     */

    /**
     * @Override public ProductDto convertToDto(Product product) {
     * ProductDto productDto = modelMapper.map(product, ProductDto.class);
     * List<Image> images = imageRepository.findByProductId(product.getId());
     * List<ImageDto> imageDtos = images.stream()
     * .map(image -> modelMapper.map(image, ImageDto.class))
     * .toList();
     * productDto.setImages(imageDtos);
     * return productDto;
     * }
     */

    @Override
    public List<ProductDto> getConvertProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }


    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();

        // Set basic product fields
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setBrand(product.getBrand());
        productDto.setPrice(product.getPrice());
        productDto.setInventory(product.getInventory());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory());

        /**
         * Model mapper is not working .. if i can use the model mapper so our boiler
         * code will reduce...
         */

        // Get images by product ID
        List<Image> images = imageRepository.findByProductId(product.getId());

        // Manually map Image -> ImageDto
        List<ImageDto> imageDtos = images.stream().map(image -> {
            ImageDto dto = new ImageDto();
            dto.setImageId(image.getId());
            dto.setImageName(image.getFileName());
            dto.setDownloadUrl(image.getDownloadUrl());
            return dto;
        }).toList();

        // Set imageDtos into productDto
        productDto.setImages(imageDtos);

        return productDto;
    }


}