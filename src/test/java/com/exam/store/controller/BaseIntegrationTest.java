package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.exam.store.controller.ControllerConstants.CATEGORY_ROOT;
import static com.exam.store.controller.ControllerConstants.DELETE_CATEGORY_PATH;
import static com.exam.store.controller.ControllerConstants.GET_CATEGORY_ID_PATH;
import static com.exam.store.controller.ControllerConstants.PRODUCT_ROOT;
import static com.exam.store.controller.ControllerConstants.UPDATE_CATEGORY_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class BaseIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    final String username = "someuser";
    final String password = "somepassword";

    String createEmptyCategoryBody() throws JsonProcessingException {
        CategoryDTO request = new CategoryDTO();
        return mapper.writeValueAsString(request);
    }

    String createEmptyProductBody() throws JsonProcessingException {
        ProductDTO request = new ProductDTO();
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


    CategoryDTO searchCategory(Long categoryId) throws Exception {
        MvcResult result = searchCategory(categoryId, status().isOk()).andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);
    }

    ResultActions searchCategory(Long categoryId, ResultMatcher expectedResult) throws Exception {
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(categoryId).toString();

        return mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedResult);
    }

    ResultActions updateCategory(Long categoryId, String body, ResultMatcher expectedResult) throws Exception {
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + UPDATE_CATEGORY_PATH)
                .buildAndExpand(categoryId).toString();

        return mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(expectedResult);
    }

    CategoryDTO updateCategory(Long categoryId, String body) throws Exception {
        MvcResult resultTwo = updateCategory(categoryId, body, status().isOk()).andReturn();
        return mapper.readValue(resultTwo.getResponse().getContentAsString(), CategoryDTO.class);
    }

    ResultActions createCategory(String body, ResultMatcher expectedResult) throws Exception {
        return mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(expectedResult);
    }

    CategoryDTO createCategory(String body) throws Exception {
        MvcResult result = createCategory(body, status().isOk()).andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);
    }

    ResultActions deleteCategory(Long categoryId, ResultMatcher expectedResult) throws Exception {
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + DELETE_CATEGORY_PATH)
                .buildAndExpand(categoryId).toString();

        return mockMvc
                .perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedResult);
    }

    ResultActions deleteCategory(Long categoryId) throws Exception {
        return deleteCategory(categoryId, status().isOk());
    }

    ResultActions createProduct(String productBody, ResultMatcher expectedResult) throws Exception {
        return mockMvc
                .perform(put(PRODUCT_ROOT).contentType(MediaType.APPLICATION_JSON).content(productBody))
                .andExpect(expectedResult);
    }

    ResultActions createProduct(String productBody) throws Exception {
        return createProduct(productBody, status().isOk());
    }

    String getUUIDName() {
        return UUID.randomUUID().toString();
    }
}
