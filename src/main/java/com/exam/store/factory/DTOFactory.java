package com.exam.store.factory;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOFactory {

    public List<CategoryDTO> createDTOs(Iterable<Category> categories) {
        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(c -> dtos.add(createDTO(c)));
        return dtos;
    }

    public CategoryDTO createDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

}
