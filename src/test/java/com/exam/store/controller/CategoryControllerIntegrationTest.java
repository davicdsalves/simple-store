package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.model.Category;
import com.exam.store.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldNotAuthorizeRetrieveCategory() throws Exception {
        Long categoryID = 1L;
        searchCategory(categoryID, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotRetrieveCategory() throws Exception {
        Long categoryID = 999L;
        searchCategory(categoryID, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForUpdate() throws Exception {
        Long categoryID = 999L;
        String body = createEmptyCategoryBody();
        updateCategory(categoryID, body, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotSaveCategoryWithSameName() throws Exception {
        String body = createCategoryBody(getUUIDName());

        createCategory(body);
        createCategory(body, status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotUpdateCategoryWithSameName() throws Exception {
        String categoryOneName = getUUIDName();
        String requestOne = createCategoryBody(categoryOneName);
        String requestTwo = createCategoryBody(getUUIDName());
        String requestThree = createCategoryBody(categoryOneName);

        createCategory(requestOne);
        CategoryDTO categoryDTO = createCategory(requestTwo);

        updateCategory(categoryDTO.getId(), requestThree, status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForDelete() throws Exception {
        Long categoryID = 999L;
        deleteCategory(categoryID, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotDeleteForProductWithCategory() throws Exception {
        String categoryBody = createCategoryBody("categoryOne");
        CategoryDTO categoryDTO = createCategory(categoryBody);

        String productBody = createProductBody(categoryDTO);
        createProduct(productBody);

        deleteCategory(categoryDTO.getId(), status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldRetrieveCategory() throws Exception {
        String categoryName = "category";
        Category category = categoryRepository.save(new Category(categoryName));

        CategoryDTO categoryDTO = searchCategory(category.getId());
        assertThat(categoryDTO.getName(), is(categoryName));
        assertThat(categoryDTO.getId(), is(category.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldSaveCategory() throws Exception {
        String body = createCategoryBody(getUUIDName());
        createCategory(body);
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldUpdateCategory() throws Exception {
        String categoryOneName = getUUIDName();
        String requestOne = createCategoryBody(categoryOneName);

        CategoryDTO categoryDTO = createCategory(requestOne);

        String categoryTwoName = "categoryTwo";
        String requestTwo = createCategoryBody(categoryTwoName);

        CategoryDTO updatedDTO = updateCategory(categoryDTO.getId(), requestTwo);
        assertThat(updatedDTO.getName(), is(categoryTwoName));
        assertThat(updatedDTO.getId(), is(categoryDTO.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldDeleteCategory() throws Exception {
        String body = createCategoryBody(getUUIDName());
        CategoryDTO categoryDTO = createCategory(body);

        deleteCategory(categoryDTO.getId());
    }

}
