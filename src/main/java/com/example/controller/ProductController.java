package com.example.controller;

import com.common.dto.UserDto;
import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/create")
    public ResponseEntity<Product> create(@Valid @RequestBody final ProductDto productDto) {
        final Product product = productDao.convertProductFromDto(productDto);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        final List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        final Optional<Product> product = productService.findById(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        final Optional<Product> productOpt = productService.findById(product.getId());
        productOpt.get().setCount(productOpt.get().getCount() - product.getSellCount());
        productOpt.get().setSellCount(product.getSellCount());
        productService.update(productOpt.get());
        return new ResponseEntity<>(productOpt.get(), HttpStatus.OK);
    }
}
