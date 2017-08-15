package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.exam.store.controller.ControllerConstants.CATEGORY_ROOT;
import static com.exam.store.controller.ControllerConstants.DELETE_CATEGORY_PATH;
import static com.exam.store.controller.ControllerConstants.GET_ALL_CATEGORIES_PATH;
import static com.exam.store.controller.ControllerConstants.GET_CATEGORY_ID_PATH;
import static com.exam.store.controller.ControllerConstants.UPDATE_CATEGORY_PATH;

@Controller
@RequestMapping(CATEGORY_ROOT)
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(GET_CATEGORY_ID_PATH)
    public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
        Optional<CategoryDTO> categoryDTO = categoryService.findById(id);
        return categoryDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(GET_ALL_CATEGORIES_PATH)
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO request) {
        CategoryDTO dto = categoryService.save(request);
        logger.info("Created category. request[{}], response[{}]", request, dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(UPDATE_CATEGORY_PATH)
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id,
                                              @RequestBody CategoryDTO request) {
        if (categoryService.exists(id)) {
            CategoryDTO categoryDTO = categoryService.update(id, request);
            logger.info("Updated category. requestID[{}], request[{}], response[{}]", id, request, categoryDTO);
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(DELETE_CATEGORY_PATH)
    public ResponseEntity delete(@PathVariable Long id) {
        if (categoryService.exists(id)) {
            categoryService.delete(id);
            logger.info("Deleted category. requestID[{}]", id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
