package com.exam.store.factory;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.model.Category;
import com.exam.store.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOFactory {

    public List<CategoryDTO> createCategoryDTOs(Iterable<Category> categories) {
        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(c -> dtos.add(createDTO(c)));
        return dtos;
    }

    public CategoryDTO createDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public List<ProductDTO> createProductDTOs(Iterable<Product> products) {
        List<ProductDTO> dtos = new ArrayList<>();
        products.forEach(c -> dtos.add(createDTO(c)));
        return dtos;
    }

    public ProductDTO createDTO(Product product) {
        CategoryDTO categoryDTO = this.createDTO(product.getCategory());
        return new ProductDTO(product.getId(), product.getName(), categoryDTO);
    }


}
