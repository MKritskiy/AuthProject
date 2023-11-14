package com.example.services;

import com.example.models.Product;
import com.example.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Transactional
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
