package com.exam.store.service;

import com.exam.store.controller.dto.ProductDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Product;
import com.exam.store.repository.CategoryRepository;
import com.exam.store.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest extends BaseServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    private DTOFactory factory = new DTOFactory();
    private ProductService target;

    @Before
    public void setUp() throws Exception {
        target = new ProductService(productRepository, categoryRepository, factory);
    }

    @Test
    public void shouldNotExistsById() throws Exception {
        Long id = 1L;
        when(productRepository.exists(id)).thenReturn(false);
        boolean exists = target.exists(id);
        assertThat(exists, is(false));
    }

    @Test
    public void shouldExistsById() throws Exception {
        Long id = 1L;
        when(productRepository.exists(id)).thenReturn(true);
        boolean exists = target.exists(id);
        assertThat(exists, is(true));
    }

    @Test
    public void shouldNotFindById() throws Exception {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        Optional<ProductDTO> dto = target.findById(id);
        assertThat(dto.isPresent(), is(false));
    }

    @Test
    public void shouldFindById() throws Exception {
        Long id = 1L;
        Product product = createProduct();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Optional<ProductDTO> dto = target.findById(id);
        assertThat(dto.isPresent(), is(true));
        assertThat(dto.get().getName(), is(product.getName()));
    }

    @Test
    public void findAllByCategory() throws Exception {
        Long categoryId = 1L;
        Product productOne = createProduct("productOne");
        Product productTwo = createProduct("productTwo");
        when(productRepository.findAllByCategoryId(categoryId)).thenReturn(Arrays.asList(productOne, productTwo));
        List<ProductDTO> dtos = target.findAllByCategory(categoryId);
        assertThat(dtos, is(not(empty())));
        assertThat(dtos, hasSize(2));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailSaveForNoCategory() throws Exception {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        ProductDTO dto = createProductDTO(id);
        target.save(dto);
    }

    @Test
    public void shouldSaveProduct() throws Exception {
        Long id = 1L;
        String productName = "product";
        when(categoryRepository.findById(id)).thenReturn(Optional.of(createCategory()));
        when(productRepository.save(any(Product.class))).thenReturn(createProduct());
        ProductDTO dto = createProductDTO(productName, id, "category");
        ProductDTO savedDTO = target.save(dto);
        verify(productRepository).save(any(Product.class));
        assertThat(savedDTO.getName(), is(productName));

    }

    @Test
    public void shouldUpdate() throws Exception {
        Long categoryId = 1L;
        Long productId = 2L;
        String productName = "newProduct";
        Product oldProduct = createProduct();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(createCategory()));
        when(productRepository.findOne(productId)).thenReturn(oldProduct);
        when(productRepository.save(any(Product.class))).thenReturn(createProduct(productName));
        ProductDTO request = createProductDTO(productName, categoryId, "category");
        ProductDTO savedDTO = target.update(productId, request);
        assertThat(savedDTO.getName(), is(productName));
    }

    @Test
    public void shouldDelete() throws Exception {
        Long id = 1L;
        target.delete(id);
        verify(productRepository).delete(id);
    }

}