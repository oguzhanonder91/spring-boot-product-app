package com.example.repository;

import com.common.repository.BaseRepository;
import com.example.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product> {
}
