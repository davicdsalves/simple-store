package com.exam.store.service;

import com.exam.store.controller.dto.CategoryDTO;
import com.exam.store.factory.DTOFactory;
import com.exam.store.model.Category;
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
public class CategoryServiceTest extends BaseServiceTest {
    @Mock
    private CategoryRepository repository;
    @Mock
    private ProductRepository productRepository;
    private DTOFactory factory = new DTOFactory();
    private CategoryService target;

    @Before
    public void setUp() throws Exception {
        target = new CategoryService(repository, productRepository, factory);
    }

    @Test
    public void shouldNotExistsById() throws Exception {
        Long id = 1L;
        when(repository.exists(id)).thenReturn(false);
        boolean exists = target.exists(id);
        assertThat(exists, is(false));
    }

    @Test
    public void shouldExistsById() throws Exception {
        Long id = 1L;
        when(repository.exists(id)).thenReturn(true);
        boolean exists = target.exists(id);
        assertThat(exists, is(true));
    }

    @Test
    public void shouldNotFindById() throws Exception {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<CategoryDTO> dto = target.findById(id);
        assertThat(dto.isPresent(), is(false));
    }

    @Test
    public void shouldFindById() throws Exception {
        Long id = 1L;
        Category category = createCategory();
        when(repository.findById(id)).thenReturn(Optional.of(category));
        Optional<CategoryDTO> dto = target.findById(id);
        assertThat(dto.isPresent(), is(true));
        assertThat(dto.get().getName(), is(category.getName()));
    }

    @Test
    public void shouldFindAll() throws Exception {
        Category categoryOne = createCategory("categoryOne");
        Category categoryTwo = createCategory("categoryTwo");
        when(repository.findAll()).thenReturn(Arrays.asList(categoryOne, categoryTwo));
        List<CategoryDTO> dtos = target.findAll();
        assertThat(dtos, is(not(empty())));
        assertThat(dtos, hasSize(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailSaveForSameNameCategory() throws Exception {
        Long id = 1L;
        String categoryName = "category";
        Category category = createCategory(categoryName);
        when(repository.findByName(categoryName)).thenReturn(Optional.of(category));
        CategoryDTO dto = new CategoryDTO(id, categoryName);
        target.save(dto);
    }

    @Test
    public void shouldSaveCategory() throws Exception {
        Long id = 1L;
        String categoryName = "category";
        CategoryDTO dto = new CategoryDTO(id, categoryName);
        when(repository.findByName(categoryName)).thenReturn(Optional.empty());
        when(repository.save(any(Category.class))).thenReturn(createCategory(categoryName));
        CategoryDTO savedDTO = target.save(dto);
        verify(repository).save(any(Category.class));
        assertThat(savedDTO.getName(), is(categoryName));
    }

    @Test
    public void shouldUpdate() throws Exception {
        Long id = 1L;
        String categoryName = "categoryNew";
        CategoryDTO dto = new CategoryDTO(id, categoryName);
        Category category = createCategory();
        when(repository.findByName(categoryName)).thenReturn(Optional.empty());
        when(repository.findOne(id)).thenReturn(category);
        when(repository.save(any(Category.class))).thenReturn(createCategory(categoryName));
        CategoryDTO savedDTO = target.save(dto);
        verify(repository).save(any(Category.class));
        assertThat(savedDTO.getName(), is(categoryName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailDeleteForCategoryWithProduct() throws Exception {
        Long id = 1L;
        Category category = createCategory();
        Product product = new Product("product", category);
        when(productRepository.findFirstByCategoryId(id)).thenReturn(Optional.of(product));
        target.delete(id);
    }

    @Test
    public void shouldDelete() throws Exception {
        Long id = 1L;
        when(productRepository.findFirstByCategoryId(id)).thenReturn(Optional.empty());
        target.delete(id);
        verify(repository).delete(id);
    }

}