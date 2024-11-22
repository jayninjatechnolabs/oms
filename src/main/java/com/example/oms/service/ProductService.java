package com.example.oms.service;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import com.example.oms.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public Product createProduct(ProductRequest productRequest) {
    Product product = new Product();
    product.setName(productRequest.getName());
    product.setBasePrice(productRequest.getBasePrice());
    return productRepository.save(product);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public List<Product> getById(List<String> productIds) {
    return productRepository.findAllById(productIds);
  }
}
