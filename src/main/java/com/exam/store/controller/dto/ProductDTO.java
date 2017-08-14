package com.exam.store.controller.dto;

import javax.validation.constraints.NotNull;

public class ProductDTO {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private CategoryDTO category;

    public ProductDTO() {
    }

    public ProductDTO(String name, CategoryDTO category) {
        this.name = name;
        this.category = category;
    }

    public ProductDTO(Long id, String name, CategoryDTO category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO getCategory() {
        return category;
    }

}
