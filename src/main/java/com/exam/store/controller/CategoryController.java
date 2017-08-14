package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/v1/category")
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
        Optional<CategoryDTO> categoryDTO = categoryService.findById(id);
        return categoryDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO request) {
        CategoryDTO dto = categoryService.save(request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id,
                                              @RequestBody CategoryDTO request) {
        if (categoryService.exists(id)) {
            CategoryDTO categoryDTO = categoryService.update(id, request);
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity errorHandler(Exception e) {
        LOGGER.error("some error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity errorHandler(IllegalArgumentException e) {
        LOGGER.error("request error", e);
        Map<String, Object> response = createSimpleResponse("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    private Map<String, Object> createSimpleResponse(String key, Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put(key, value);
        return response;
    }

}
