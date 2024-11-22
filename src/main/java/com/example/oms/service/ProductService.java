package com.example.oms.service;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import java.util.List;

public interface ProductService {
  Product createProduct(ProductRequest productRequest);

  List<Product> getAllProducts();

  List<Product> getById(List<String> productIds);
}
