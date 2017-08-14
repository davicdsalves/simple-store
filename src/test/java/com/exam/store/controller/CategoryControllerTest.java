package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
import com.exam.store.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import static com.exam.store.controller.ControllerConstants.CATEGORY_ROOT;
import static com.exam.store.controller.ControllerConstants.DELETE_CATEGORY_PATH;
import static com.exam.store.controller.ControllerConstants.GET_CATEGORY_ID_PATH;
import static com.exam.store.controller.ControllerConstants.PRODUCT_ROOT;
import static com.exam.store.controller.ControllerConstants.UPDATE_CATEGORY_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    private ObjectMapper mapper = new ObjectMapper();
    private final String username = "someuser";
    private final String password = "somepassword";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldNotAuthorizeRetrieveCategory() throws Exception {
        Long categoryID = 1L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(categoryID).toString();

        mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotRetrieveCategory() throws Exception {
        Long categoryID = 999L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(categoryID).toString();

        mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForUpdate() throws Exception {
        Long categoryID = 999L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + UPDATE_CATEGORY_PATH)
                .buildAndExpand(categoryID).toString();
        String body = createEmptyCategoryBody();

        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotSaveCategoryWithSameName() throws Exception {
        CategoryDTO request = new CategoryDTO();
        request.setName("categoryOne");
        String body = mapper.writeValueAsString(request);

        mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

        mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotUpdateCategoryWithSameName() throws Exception {
        String categoryOneName = "categoryOne";
        String requestOne = createCategoryBody(categoryOneName);
        String requestTwo = createCategoryBody("categoryTwo");

        mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(requestOne))
                .andExpect(status().isOk());
        MvcResult result = mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(requestTwo))
                .andExpect(status().isOk()).andReturn();
        CategoryDTO categoryDTO = mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);

        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + UPDATE_CATEGORY_PATH)
                .buildAndExpand(categoryDTO.getId()).toString();
        String requestThree = createCategoryBody(categoryOneName);

        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestThree))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForDelete() throws Exception {
        Long categoryID = 999L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + DELETE_CATEGORY_PATH)
                .buildAndExpand(categoryID).toString();

        mockMvc
                .perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotDeleteForProductWithCategory() throws Exception {
        String categoryBody = createCategoryBody("categoryOne");
        MvcResult savedCategoryResult = mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(categoryBody))
                .andExpect(status().isOk()).andReturn();
        CategoryDTO categoryDTO = mapper.readValue(savedCategoryResult.getResponse().getContentAsString(), CategoryDTO.class);

        String productBody = createProductBody(categoryDTO);
        mockMvc
                .perform(put(PRODUCT_ROOT).contentType(MediaType.APPLICATION_JSON).content(productBody))
                .andExpect(status().isOk());

        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + DELETE_CATEGORY_PATH)
                .buildAndExpand(categoryDTO.getId()).toString();
        mockMvc
                .perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldRetrieveCategory() throws Exception {
        String categoryName = "category";
        Category category = categoryRepository.save(new Category(categoryName));

        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(category.getId()).toString();

        MvcResult result = mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        CategoryDTO categoryDTO = mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);
        assertThat(categoryDTO.getName(), is(categoryName));
        assertThat(categoryDTO.getId(), is(category.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldSaveCategory() throws Exception {
        String body = createCategoryBody("categoryOne");
        mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldUpdateCategory() throws Exception {
        String categoryOneName = "categoryOne";
        String requestOne = createCategoryBody(categoryOneName);

        MvcResult result = mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(requestOne))
                .andExpect(status().isOk()).andReturn();
        CategoryDTO categoryDTO = mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);

        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + UPDATE_CATEGORY_PATH)
                .buildAndExpand(categoryDTO.getId()).toString();
        String categoryTwoName = "categoryTwo";
        String requestTwo = createCategoryBody(categoryTwoName);

        MvcResult resultTwo = mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestTwo))
                .andExpect(status().isOk()).andReturn();
        CategoryDTO updatedDTO = mapper.readValue(resultTwo.getResponse().getContentAsString(), CategoryDTO.class);
        assertThat(updatedDTO.getName(), is(categoryTwoName));
        assertThat(updatedDTO.getId(), is(categoryDTO.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldDeleteCategory() throws Exception {
        String body = createCategoryBody("categoryOne");
        MvcResult result = mockMvc
                .perform(put(CATEGORY_ROOT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn();
        CategoryDTO categoryDTO = mapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + DELETE_CATEGORY_PATH)
                .buildAndExpand(categoryDTO.getId()).toString();

        mockMvc
                .perform(delete(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    private String createEmptyCategoryBody() throws JsonProcessingException {
        CategoryDTO request = new CategoryDTO();
        return mapper.writeValueAsString(request);
    }

    private String createCategoryBody(String name) throws JsonProcessingException {
        CategoryDTO request = new CategoryDTO();
        request.setName(name);
        return mapper.writeValueAsString(request);
    }

    private String createProductBody(CategoryDTO categoryDTO) throws JsonProcessingException {
        ProductDTO request = new ProductDTO("product", categoryDTO);
        return mapper.writeValueAsString(request);
    }


}
