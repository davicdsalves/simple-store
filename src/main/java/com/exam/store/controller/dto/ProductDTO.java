package com.exam.store.controller.dto;

import javax.validation.constraints.NotNull;

public class ProductDTO {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Long price;
    private String currency;
    @NotNull
    private CategoryDTO category;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Long price, CategoryDTO category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getPrice() {
        return price;
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

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", category=" + category +
                '}';
    }
}
