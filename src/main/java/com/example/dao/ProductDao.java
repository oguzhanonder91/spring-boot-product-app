package com.example.dao;

import com.common.exception.BaseNotFoundException;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductDao {

    @Autowired
    private ProductService productService;

    public Product convertProductFromDto(ProductDto productDto) {
        Product product = new Product();
        product.setCount(productDto.getCount());
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return product;
    }

    public Product sellProduct(Product product) {
        Optional<Product> p = productService.findById(product.getId());
        if (p.isPresent()) {
            p.get().setCount(p.get().getCount() - product.getSellCount());
            p.get().setSellCount(product.getSellCount());
            return p.get();
        }else{
            throw new BaseNotFoundException("Id Bulunamadı");
        }
    }

}
