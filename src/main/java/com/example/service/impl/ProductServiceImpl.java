package com.example.service.impl;

import com.common.service.impl.BaseServiceImpl;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductDto> implements ProductService {
}
