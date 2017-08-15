package com.exam.store.controller;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.fixer.FixerUrl;
import com.exam.store.model.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest extends BaseIntegrationTest {
    private MockRestServiceServer fixer;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FixerUrl fixerUrl;

    @Before
    public void setUp() throws Exception {
        fixer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldNotAuthorizeRetrieveProduct() throws Exception {
        Long productId = 1L;
        searchProduct(productId, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotRetrieveProduct() throws Exception {
        Long productId = 999L;
        searchProduct(productId, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForUpdate() throws Exception {
        Long productId = 999L;
        String body = createEmptyProductBody();
        updateProduct(productId, body, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldNotFindForDelete() throws Exception {
        Long categoryID = 999L;
        deleteProduct(categoryID, status().isNotFound());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldRetrieveProduct() throws Exception {
        CategoryDTO category = createRandomCategory();
        String body = createProductBody(category);
        ProductDTO product = createProduct(body);

        ProductDTO productDTO = searchProduct(product.getId());
        assertThat(productDTO.getName(), is(product.getName()));
        assertThat(productDTO.getId(), is(product.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldSaveProduct() throws Exception {
        CategoryDTO category = createRandomCategory();
        String body = createProductBody(category);
        createProduct(body);
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldUpdateProduct() throws Exception {
        CategoryDTO categoryOne = createRandomCategory();
        String requestOne = createProductBody(categoryOne);
        ProductDTO productOne = createProduct(requestOne);

        CategoryDTO categoryTwo = createRandomCategory();
        String requestTwo = createProductBody(categoryTwo);

        ProductDTO updatedDTO = updateProduct(productOne.getId(), requestTwo);
        assertThat(updatedDTO.getName(), is(productOne.getName()));
        assertThat(updatedDTO.getId(), is(productOne.getId()));
        assertThat(updatedDTO.getCategory().getName(), is(categoryTwo.getName()));
        assertThat(updatedDTO.getCategory().getId(), is(categoryTwo.getId()));
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldDeleteProduct() throws Exception {
        CategoryDTO category = createRandomCategory();
        String body = createProductBody(category);
        ProductDTO product = createProduct(body);

        deleteProduct(product.getId());
    }

    @Test
    @WithMockUser(username = username, password = password)
    public void shouldSaveProductWithDiffCurrency() throws Exception {
        String currency = Currency.BRL.toString();
        URI fixerURI = fixerUrl.createSearchURI(currency);
        fixer.expect(requestTo(fixerURI))
                .andRespond(withSuccess(new ClassPathResource("fixer-response.json"), MediaType.APPLICATION_JSON));

        CategoryDTO category = createRandomCategory();
        String body = createProductBody(category, 100L, currency);
        ProductDTO product = createProduct(body);
        assertThat(product.getPrice(), is(26L)); //1,00 BRL -> 0,26 EUR
    }

}
