package com.exam.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static com.exam.store.controller.ControllerConstants.CATEGORY_ROOT;
import static com.exam.store.controller.ControllerConstants.GET_CATEGORY_ID_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Value("${store.user}")
    private String username;
    @Value("${store.password}")
    private String password;

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
        Long categoryID = 1L;
        String url = UriComponentsBuilder.fromUriString(CATEGORY_ROOT + GET_CATEGORY_ID_PATH)
                .buildAndExpand(categoryID).toString();

        mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }
}
