package com.Mini.Mini.repository;


import com.Mini.Mini.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findDiscountPercentageById(Long categoryId);
}
