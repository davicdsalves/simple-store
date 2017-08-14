package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Category;
import com.exam.store.model.Product;
import com.exam.store.repository.CategoryRepository;
import com.exam.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository repository;
    private ProductRepository productRepository;
    private DTOFactory factory;

    public CategoryService(CategoryRepository repository,
                           ProductRepository productRepository,
                           DTOFactory factory) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.factory = factory;
    }

    public boolean exists(Long id) {
        return repository.exists(id);
    }

    public Optional<CategoryDTO> findById(Long id) {
        Optional<Category> category = repository.findById(id);
        return category.map(factory::createDTO);
    }

    public List<CategoryDTO> findAll() {
        Iterable<Category> categories = repository.findAll();
        return factory.createCategoryDTOs(categories);
    }

    public CategoryDTO save(CategoryDTO dto) {
        validateCategory(dto);
        Category category = new Category(dto.getName());
        return save(category);
    }

    public CategoryDTO update(Long id, CategoryDTO request) {
        validateCategory(request);
        Category category = repository.findOne(id);
        category.setName(request.getName());
        return save(category);
    }

    public void delete(Long id) {
        //melhorar para ser -> exist by category id
        Optional<Product> productByCategoryId = productRepository.findFirstByCategoryId(id);
        if (productByCategoryId.isPresent()) {
            String errorMessage = "Not allowed to remove a category that has products related to it.";
            throw new IllegalArgumentException(errorMessage);
        }
        repository.delete(id);
    }

    private void validateCategory(CategoryDTO dto) {
        Optional<Category> validateCategory = repository.findByName(dto.getName());
        if (validateCategory.isPresent()) {
            String errorMessage = "There is already a category named %s";
            throw new IllegalArgumentException(String.format(errorMessage, dto.getName()));
        }
    }

    private CategoryDTO save(Category category) {
        return factory.createDTO(repository.save(category));
    }
}
