package com.kodnest.best_shop.controller;
import com.kodnest.best_shop.model.Category;
import com.kodnest.best_shop.response.ApiResponse;
import com.kodnest.best_shop.service.category.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/category")
public class Categorycon {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categoryList = categoryService.getAllCategories();
            return  ResponseEntity.ok().body(new ApiResponse("All category fetched successfully",categoryList));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",INTERNAL_SERVER_ERROR));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        try{
            Category newCategory = categoryService.addCategory(name);
            return  ResponseEntity.ok().body(new ApiResponse("success",newCategory));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try{
            Category data = categoryService.getCategoryById(id);
            return ResponseEntity.ok().body(new ApiResponse("success", data));
        }catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category data = categoryService.getCategoryByName(name);
            return ResponseEntity.ok().body(new ApiResponse("success", data));

        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().body(new ApiResponse("deleted Successfully", null));

        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategoryById(@RequestBody Category category, @PathVariable Long id) {
        try {
            Category newUpdatedData = categoryService.updateCategory(category,id);
            return ResponseEntity.ok().body(new ApiResponse("updated Successfully", newUpdatedData));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}

