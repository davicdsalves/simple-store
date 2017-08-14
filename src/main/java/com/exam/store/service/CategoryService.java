package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Optional<CategoryDTO> findById(Long id) {
        Optional<Category> category = repository.findById(id);
        return category.map(this::createDTO);
    }

    private CategoryDTO createDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
