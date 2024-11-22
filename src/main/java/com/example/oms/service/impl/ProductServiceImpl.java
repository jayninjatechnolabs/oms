package com.example.oms.service.impl;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import com.example.oms.repository.ProductRepository;
import com.example.oms.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  @Transactional
  public Product createProduct(ProductRequest productRequest) {
    Product product = new Product();
    product.setName(productRequest.getName());
    product.setBasePrice(productRequest.getBasePrice());
    return productRepository.save(product);
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getById(List<String> productIds) {
    return productRepository.findAllById(productIds);
  }
}
