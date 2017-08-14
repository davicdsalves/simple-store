package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<CategoryDTO> findAll() {
        Iterable<Category> categories = repository.findAll();
        return createDTOs(categories);
    }

    private List<CategoryDTO> createDTOs(Iterable<Category> categories) {
        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(c -> dtos.add(createDTO(c)));
        return dtos;
    }

    private CategoryDTO createDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
