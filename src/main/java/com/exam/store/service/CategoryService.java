package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Category;
import com.exam.store.model.Product;
import com.exam.store.repository.CategoryRepository;
import com.exam.store.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        if (dto.getParentID() != null) {
            validateParent(dto.getParentID());
            category.setParent(new Category(dto.getParentID()));
        }
        return save(category);
    }

    public CategoryDTO update(Long id, CategoryDTO request) {
        validateCategory(request);
        Category categoryToUpdate = repository.findOne(id);
        if (!categoryToUpdate.getName().equalsIgnoreCase(request.getName())
                && StringUtils.hasText(request.getName())) {
            categoryToUpdate.setName(request.getName());
        }

        Long parentId = request.getParentID();
        if (parentId != null) {
            validateParent(parentId);
            Category parent = repository.findOne(parentId);
            validateCircularReference(categoryToUpdate, parent);
            categoryToUpdate.setParent(parent);
        }

        return save(categoryToUpdate);
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

    private void validateParent(Long parentId) {
        if (!exists(parentId)) {
            throw new IllegalArgumentException("Invalid parentId");
        }
    }

    private void validateCircularReference(Category category, Category parent) {
        if (isCircularReference(category, parent)) {
            String errorMessage = String.format("Circular reference. Category[%d] has Category[%d] as parent.", parent.getId(), category.getId());
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isCircularReference(Category categoryToUpdate, Category parent) {
        return (parent.getParent() != null && parent.getParent().getId().equals(categoryToUpdate.getId())) ||
                categoryToUpdate.getId().equals(parent.getId());
    }

    private CategoryDTO save(Category category) {
        return factory.createDTO(repository.save(category));
    }
}
