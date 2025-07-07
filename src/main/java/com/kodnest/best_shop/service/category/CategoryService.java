package com.kodnest.best_shop.service.category;

import com.kodnest.best_shop.exceptions.AlreadyExistException;
import com.kodnest.best_shop.exceptions.CategoryNotFoundException;
import com.kodnest.best_shop.model.Category;
import com.kodnest.best_shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements  ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        try {
            return categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException("Category Not Found!!"));
        } catch (CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    @Override
    public Category addCategory(Category category) {
        try {
            return Optional.of(category)
                    .filter(c->!categoryRepository.existsByName(c.getName()))
                    .map(categoryRepository::save)
                    .orElseThrow(()-> new AlreadyExistException("Category already exists!!"));
        } catch (AlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        try {
            return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
                oldCategory.setName(category.getName());
                return categoryRepository.save(oldCategory);
            }).orElseThrow(()-> new CategoryNotFoundException("Category Not found!!"));
        } catch (CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
                ()->{
                    try {
                        throw new CategoryNotFoundException("Category not found");
                    } catch (CategoryNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } );
    }
}
