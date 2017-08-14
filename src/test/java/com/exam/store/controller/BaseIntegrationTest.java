package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

class BaseIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    final String username = "someuser";
    final String password = "somepassword";

    String createEmptyCategoryBody() throws JsonProcessingException {
        CategoryDTO request = new CategoryDTO();
        return mapper.writeValueAsString(request);
    }

    String createCategoryBody(String name) throws JsonProcessingException {
        CategoryDTO request = new CategoryDTO();
        request.setName(name);
        return mapper.writeValueAsString(request);
    }

    String createProductBody(CategoryDTO categoryDTO) throws JsonProcessingException {
        ProductDTO request = new ProductDTO("product", categoryDTO);
        return mapper.writeValueAsString(request);
    }

    String getUUIDName() {
        return UUID.randomUUID().toString();
    }
}
