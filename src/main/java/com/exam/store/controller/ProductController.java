package com.exam.store.controller;

import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.service.ProductService;
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
import java.util.Optional;

@Controller
@RequestMapping("/v1/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.findById(id);
        return productDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO request) {
        ProductDTO dto = productService.save(request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                              @RequestBody ProductDTO request) {
        if (productService.exists(id)) {
            ProductDTO ProductDTO = productService.update(id, request);
            return ResponseEntity.ok(ProductDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (productService.exists(id)) {
            productService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
}
