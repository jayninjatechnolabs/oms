package com.example.oms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.oms.dto.ProductRequest;
import com.example.oms.entity.Product;
import com.example.oms.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductServiceImpl productService;

  private ObjectMapper objectMapper;
  private Product product;
  private ProductRequest productRequest;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();

    product = new Product();
    product.setProductId(UUID.randomUUID().toString());
    product.setName("Test Product");
    product.setBasePrice(100.0);

    productRequest = new ProductRequest();
    productRequest.setName("Test Product");
    productRequest.setBasePrice(100.0);
  }

  @Test
  public void testCreateProduct() throws Exception {
    when(productService.createProduct(any(ProductRequest.class))).thenReturn(product);

    mockMvc.perform(post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.productId").value(product.getProductId()))
        .andExpect(jsonPath("$.name").value(product.getName()))
        .andExpect(jsonPath("$.basePrice").value(product.getBasePrice()));

    verify(productService, times(1)).createProduct(any(ProductRequest.class));
  }

  @Test
  public void testGetAllProducts() throws Exception {
    List<Product> products = new ArrayList<>();
    products.add(product);

    when(productService.getAllProducts()).thenReturn(products);

    mockMvc.perform(get("/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].productId").value(product.getProductId()))
        .andExpect(jsonPath("$.[0].name").value(product.getName()))
        .andExpect(jsonPath("$.[0].basePrice").value(product.getBasePrice()));

    verify(productService, times(1)).getAllProducts();
  }

  @Test
  public void testGetAllProducts_NoProducts() throws Exception {
    when(productService.getAllProducts()).thenReturn(new ArrayList<>());

    mockMvc.perform(get("/products"))
        .andExpect(status().isNoContent());

    verify(productService, times(1)).getAllProducts();
  }
}
