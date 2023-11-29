package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Category;
import com.Mini.Mini.repository.CategoryRepository;
import com.Mini.Mini.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public void addCategories(Category category){
        categoryRepository.save(category);

    }
    public void removeCategoryById(int id){
        categoryRepository.deleteById(id);
    }
    public Optional<Category> findCategoryById(@PathVariable int id){
        return categoryRepository.findById(id);
    }

    public double getCategoryDiscountPercentage(int categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getDiscountPercentage)
                .orElse(0.0);
    }

    public Object getAllCategoryDiscount() {
        return categoryRepository.findAll();
    }


    public List<Category> getAllCategoriesWithDiscount() {
        return categoryRepository.findAll();
    }

    public Category findDiscountPercentageById(Long categoryId) {
        return categoryRepository.findDiscountPercentageById(categoryId);
    }
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
}
