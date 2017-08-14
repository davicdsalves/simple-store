package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.model.Category;
import com.exam.store.model.Product;

public class BaseServiceTest {

    Product createProduct(String name) {
        return new Product(name, createCategory());
    }

    Product createProduct() {
        return new Product("product", createCategory());
    }

    Category createCategory() {
        return createCategory("category");
    }

    Category createCategory(String name) {
        return new Category(name);
    }

    ProductDTO createProductDTO(String name, Long categoryId) {
        return new ProductDTO(1L, name, new CategoryDTO(categoryId, "category"));
    }

    ProductDTO createProductDTO(Long categoryId) {
        return createProductDTO("product", categoryId);
    }


}
