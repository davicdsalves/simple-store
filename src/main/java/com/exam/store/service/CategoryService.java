package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository repository;
    private DTOFactory factory;

    public CategoryService(CategoryRepository repository, DTOFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public Optional<CategoryDTO> findById(Long id) {
        Optional<Category> category = repository.findById(id);
        return category.map(factory::createDTO);
    }

    public List<CategoryDTO> findAll() {
        Iterable<Category> categories = repository.findAll();
        return factory.createDTOs(categories);
    }

}
