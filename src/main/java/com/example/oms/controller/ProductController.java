package com.example.oms.controller;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import com.example.oms.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
    Product product = productService.createProduct(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> allProducts = productService.getAllProducts();
    if (allProducts.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(allProducts);
  }
}
