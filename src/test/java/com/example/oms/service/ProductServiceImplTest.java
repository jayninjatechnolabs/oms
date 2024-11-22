package com.example.oms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import com.example.oms.repository.ProductRepository;
import com.example.oms.service.impl.ProductServiceImpl;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  private Product product;
  private ProductRequest productRequest;

  @BeforeEach
  public void setUp() {
    product = new Product();
    product.setProductId(UUID.randomUUID().toString());
    product.setName("Test Product");
    product.setBasePrice(100.0);

    productRequest = new ProductRequest();
    productRequest.setName("Test Product");
    productRequest.setBasePrice(100.0);
  }

  @Test
  public void testCreateProduct() {
    when(productRepository.save(any(Product.class))).thenReturn(product);

    Product createdProduct = productService.createProduct(productRequest);

    assertNotNull(createdProduct);
    assertEquals(product.getName(), createdProduct.getName());
    assertEquals(product.getBasePrice(), createdProduct.getBasePrice());

    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  public void testGetAllProducts() {
    List<Product> products = new ArrayList<>();
    products.add(product);

    when(productRepository.findAll()).thenReturn(products);

    List<Product> retrievedProducts = productService.getAllProducts();

    assertNotNull(retrievedProducts);
    assertEquals(1, retrievedProducts.size());
    assertEquals(product.getName(), retrievedProducts.get(0).getName());

    verify(productRepository, times(1)).findAll();
  }

  @Test
  public void testGetById() {
    List<String> productIds = List.of("1");
    List<Product> products = new ArrayList<>();
    products.add(product);

    when(productRepository.findAllById(productIds)).thenReturn(products);

    List<Product> retrievedProducts = productService.getById(productIds);

    assertNotNull(retrievedProducts);
    assertEquals(1, retrievedProducts.size());
    assertEquals(product.getName(), retrievedProducts.get(0).getName());

    verify(productRepository, times(1)).findAllById(productIds);
  }
}
