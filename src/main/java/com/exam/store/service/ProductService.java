package com.exam.store.service;

import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Category;
import com.exam.store.model.Currency;
import com.exam.store.model.Product;
import com.exam.store.repository.CategoryRepository;
import com.exam.store.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository repository;
    private CategoryRepository categoryRepository;
    private DTOFactory factory;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository, DTOFactory factory) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.factory = factory;
    }

    public boolean exists(Long id) {
        return repository.exists(id);
    }

    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.map(factory::createDTO);
    }

    public List<ProductDTO> findAllByCategory(Long categoryId) {
        Iterable<Product> products = repository.findAllByCategoryId(categoryId);
        return factory.createProductDTOs(products);
    }

    public ProductDTO save(ProductDTO dto) {
        Category category = validateProduct(dto);
        Product Product = new Product(dto.getName(), category);
        return save(Product);
    }

    public ProductDTO update(Long id, ProductDTO request) {
        Category category = validateProduct(request);
        Product product = repository.findOne(id);
        product.setName(request.getName());
        product.setCategory(category);
        return save(product);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    private Category validateProduct(ProductDTO dto) {
        Long categoryId = dto.getCategory().getId();
        Optional<Category> validateCategory = categoryRepository.findById(categoryId);
        if (!validateCategory.isPresent()) {
            String errorMessage = "There is no category with id %d";
            throw new IllegalArgumentException(String.format(errorMessage, categoryId));
        }
        validateCurrency(dto.getCurrency());
        return validateCategory.get();
    }

    private void validateCurrency(String productCurrency) {
        if (StringUtils.hasText(productCurrency)) {
            Currency currency = Currency.get(productCurrency);
            if (Currency.UNKNOWN.equals(currency)) {
                String errorMessage = "Currency %s is not allowed";
                throw new IllegalArgumentException(String.format(errorMessage, productCurrency));
            }
        }
    }

    private ProductDTO save(Product Product) {
        return factory.createDTO(repository.save(Product));
    }


}
