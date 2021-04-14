package com.example.dao;

import com.common.exception.BaseNotFoundException;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    @Autowired
    private ProductService productService;

    public Product convertProductFromDto(ProductDto productDto) {
        Product product = new Product();
        product.setRemaining(productDto.getRemaining());
        product.setTotal(productDto.getTotal());
        product.setSellCount(productDto.getSellCount());
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setProfit((productDto.getSellCount() * productDto.getSellPrice()) - (productDto.getPrice() * productDto.getTotal()));
        return product;
    }

    public Product create(Product product) {
        return productService.saveForEntity(product);
    }

    public Product update(Product product) {
        return productService.updateForEntity(product);
    }

    public Optional<Product> findById(String id) {
        return productService.findByIdActiveForEntity(id);
    }

    public List<Product> getAllProduct() {
        return productService.findAllForEntity();
    }

    public Product sellProduct(Product product) {
        Optional<Product> p = productService.findByIdActiveForEntity(product.getId());
        if (p.isPresent()) {
            p.get().setRemaining(p.get().getRemaining() - product.getSellCount());
            p.get().setSellCount(p.get().getSellCount() + product.getSellCount());
            p.get().setSellPrice(product.getSellPrice());
            p.get().setProfit(p.get().getProfit() + (product.getSellCount() * product.getSellPrice()));
            return p.get();
        } else {
            throw new BaseNotFoundException("Id Not Found");
        }
    }

}
