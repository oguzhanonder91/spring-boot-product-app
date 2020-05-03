package com.example.dao;

import com.example.dto.ProductDto;
import com.example.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    public Product convertProductFromDto(ProductDto productDto){
        Product product = new Product();
        product.setCount(productDto.getCount());
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return product;
    }
}
