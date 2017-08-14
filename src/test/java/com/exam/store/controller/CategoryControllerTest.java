package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
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
import static com.exam.store.controller.ControllerConstants.GET_CATEGORY_ID_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

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
    @WithMockUser(username = "someuser", password = "somepassword")
    public void shouldNotRetrieveCategory() throws Exception {
        Long categoryID = 999L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(categoryID).toString();

        mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithMockUser(username = "someuser", password = "somepassword")
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


}
