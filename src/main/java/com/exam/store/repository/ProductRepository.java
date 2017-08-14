package com.exam.store.repository;

import com.exam.store.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Optional<Product> findFirstByCategoryId(Long categoryId);

    List<Product> findAllByCategoryId(Long categoryId);
}
