package com.example.controller;

import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
@MyServiceGroupAnnotation(name = "Ürün Yönetimi",path = "product")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @PostMapping(path = "/create")
    @MyServiceAnnotation(name = "Ürün Girişi", path = "/create", type = MethodType.POST, permissionRoles = {"ADMIN"})
    public ResponseEntity<Product> create(@Valid @RequestBody final ProductDto productDto) {
        final Product product = productDao.convertProductFromDto(productDto);
        Product created = productDao.create(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    @MyServiceAnnotation(name = "Ürün Listeleme", path = "/all", type = MethodType.GET, permissionRoles = {"ADMIN","USER"})
    public ResponseEntity<List<Product>> getAllProducts() {
        final List<Product> products = productDao.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @MyServiceAnnotation(name = "İd ye Göre Ürün Listeleme", path = "/{}", type = MethodType.GET, permissionRoles = {"ADMIN","USER"})
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        final Optional<Product> product = productDao.findById(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/sell")
    @MyServiceAnnotation(name = "Ürün Çıkarma", path = "/sell", type = MethodType.PUT, permissionRoles = {"ADMIN"})
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        Product p = productDao.sellProduct(product);
        productDao.update(p);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
}
