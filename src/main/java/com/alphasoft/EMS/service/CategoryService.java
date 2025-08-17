package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Category;
import com.alphasoft.EMS.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional
    public void save(Category category){
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(long id){
        categoryRepository.deleteById(id);
    }

    public List<Category> findAllByUserId(Long userId){
        return categoryRepository.findAllByUserId(userId).orElse(null);
    }

    public Category findCategoryByUserIdAndCategoryName(Long userId, String categoryName){
        return categoryRepository.findCategoryByUserIdAndCategoryName(userId, categoryName).orElse(null);
    }

}
