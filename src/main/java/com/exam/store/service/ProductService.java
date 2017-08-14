package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Product;
import com.exam.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository repository;
    private DTOFactory factory;

    public ProductService(ProductRepository repository, DTOFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.map(this::createDTO);
    }

    public List<ProductDTO> findAllByCategory(Long categoryId) {
        Iterable<Product> categories = repository.findAllByCategoryId(categoryId);
        return createDTOs(categories);
    }

    //melhorar para ser -> exist by category id
    public Optional<ProductDTO> findByCategoryId(Long id) {
        Optional<Product> product = repository.findFirstByCategoryId(id);
        return product.map(this::createDTO);
    }

    private List<ProductDTO> createDTOs(Iterable<Product> products) {
        List<ProductDTO> dtos = new ArrayList<>();
        products.forEach(c -> dtos.add(createDTO(c)));
        return dtos;
    }

    private ProductDTO createDTO(Product product) {
        CategoryDTO categoryDTO = factory.createDTO(product.getCategory());
        return new ProductDTO(product.getName(), categoryDTO);
    }

}
