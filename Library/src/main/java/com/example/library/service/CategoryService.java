package com.example.library.service;

import com.example.library.model.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryService {
    Category save(Category category);

    Category findById(Long id);

    Category update(Category category);

    List<Category>findAllByActivatedTrue();

    List<Category>findAll();

    void deleteById(Long id);

    void enableByID(Long id);

    Long countAllCategories();

    Category getCartById(Long id);


}
