package com.exam.store.controller;

import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.service.ProductService;
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
import java.util.Optional;

import static com.exam.store.controller.ControllerConstants.DELETE_PRODUCT_PATH;
import static com.exam.store.controller.ControllerConstants.GET_PRODUCT_ID_PATH;
import static com.exam.store.controller.ControllerConstants.PRODUCT_ROOT;
import static com.exam.store.controller.ControllerConstants.UPDATE_PRODUCT_PATH;

@Controller
@RequestMapping(PRODUCT_ROOT)
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = GET_PRODUCT_ID_PATH)
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.findById(id);
        return productDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO request) {
        ProductDTO dto = productService.save(request);
        logger.info("Created product. request[{}], response[{}]", request, dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(UPDATE_PRODUCT_PATH)
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                              @RequestBody ProductDTO request) {
        if (productService.exists(id)) {
            ProductDTO productDTO = productService.update(id, request);
            logger.info("Updated product. requestID[{}], request[{}], response[{}]", id, request, productDTO);
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(DELETE_PRODUCT_PATH)
    public ResponseEntity delete(@PathVariable Long id) {
        if (productService.exists(id)) {
            productService.delete(id);
            logger.info("Deleted product. requestID[{}]", id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
}
