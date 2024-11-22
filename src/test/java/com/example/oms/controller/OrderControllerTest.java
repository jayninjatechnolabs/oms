package com.example.oms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oms.dto.ItemRequest;
import com.example.oms.dto.OrderRequest;
import com.example.oms.dto.OrderStatus;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.entity.Product;
import com.example.oms.service.impl.CustomerServiceImpl;
import com.example.oms.service.impl.OrderServiceImpl;
import com.example.oms.service.impl.ProductServiceImpl;
import com.example.oms.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderServiceImpl orderService;

  @MockBean
  private ProductServiceImpl productService;

  @MockBean
  private CustomerServiceImpl customerService;

  private ObjectMapper objectMapper;
  private Order order;
  private OrderRequest orderRequest;
  private Customer customer;
  private Product product;
  private OrderItem orderItem;
  private Map<String, Product> productMap;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();

    customer = new Customer();
    customer.setCustomerId(UUID.randomUUID().toString());
    customer.setName("John Doe");
    customer.setAccountNumber(123456789L);
    customer.setBankName("Bank of Example");
    customer.setOrderCount(0);

    product = new Product();
    product.setProductId(UUID.randomUUID().toString());
    product.setName("Test Product");
    product.setBasePrice(100.0);

    orderItem = new OrderItem();
    orderItem.setOrderItemId(UUID.randomUUID().toString());
    orderItem.setProduct(product);
    orderItem.setQuantity(2);
    orderItem.setSubtotal(200.0);

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    order = new Order(customer, orderItems);
    order.setOrderId(UUID.randomUUID().toString());
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);
    order.setTotalDiscount(0.0);
    order.setTotalPrice(200.0);
    order.setTotalPriceAfterDiscount(200.0);

    orderRequest = new OrderRequest();
    orderRequest.setCustomerId(customer.getCustomerId());
    ItemRequest itemRequest = new ItemRequest();
    itemRequest.setProductId(product.getProductId());
    itemRequest.setQuantity(2);
    orderRequest.setItems(List.of(itemRequest));

    productMap = new HashMap<>();
    productMap.put("1", product);
  }

  @Test
  public void testCreateOrder() throws Exception {
    when(customerService.getById(anyString())).thenReturn(customer);
    when(productService.getById(anyList())).thenReturn(List.of(product));
    when(orderService.createOrder(anyList(), any(Customer.class))).thenReturn(order);

    mockMvc.perform(post(Constants.API.API_VERSION_V1 + Constants.API.ORDERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string(order.getOrderId()));

    verify(customerService, times(1)).getById(anyString());
    verify(productService, times(1)).getById(anyList());
    verify(orderService, times(1)).createOrder(anyList(), any(Customer.class));
  }

  @Test
  public void testGetOrder() throws Exception {
    when(orderService.getById(anyString())).thenReturn(order);

    mockMvc.perform(
            get(Constants.API.API_VERSION_V1 + Constants.API.ORDERS + Constants.API.ORDER_BY_ID,
                order.getOrderId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
        .andExpect(jsonPath("$.orderDate").isNotEmpty())
        .andExpect(jsonPath("$.status").value(order.getStatus().toString()))
        .andExpect(jsonPath("$.totalDiscount").value(order.getTotalDiscount()))
        .andExpect(jsonPath("$.totalPrice").value(order.getTotalPrice()))
        .andExpect(jsonPath("$.totalPriceAfterDiscount").value(order.getTotalPriceAfterDiscount()))
        .andExpect(
            jsonPath("$.items.[0].orderItemId").value(order.getItems().get(0).getOrderItemId()))
        .andExpect(jsonPath("$.items.[0].product.productId").value(
            order.getItems().get(0).getProduct().getProductId()))
        .andExpect(jsonPath("$.items.[0].quantity").value(order.getItems().get(0).getQuantity()))
        .andExpect(jsonPath("$.items.[0].subtotal").value(order.getItems().get(0).getSubtotal()));

    verify(orderService, times(1)).getById(anyString());
  }
}
