package com.exam.store.controller.dto;

import javax.validation.constraints.NotNull;

public class ProductDTO {
    @NotNull
    private String name;
    @NotNull
    private CategoryDTO category;

    public ProductDTO(String name, CategoryDTO category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO getCategory() {
        return category;
    }

}
