package com.example.oms.controller;

import com.example.oms.dto.OrderRequest;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.entity.Product;
import com.example.oms.service.CustomerService;
import com.example.oms.service.OrderService;
import com.example.oms.service.ProductService;
import com.example.oms.util.Constants;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API.API_VERSION_V1 + Constants.API.ORDERS)
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final ProductService productService;
  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
    Map<String, Product> productMap = productService.getById(orderRequest.getProductIds())
        .stream()
        .collect(Collectors.toMap(Product::getProductId, Function.identity()));

    List<OrderItem> items = orderRequest.getItems()
        .stream()
        .map(item -> new OrderItem(null, productMap.get(item.getProductId()), item.getQuantity()))
        .toList();

    Customer customer = customerService.getById(orderRequest.getCustomerId());
    Order order = orderService.createOrder(items, customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(order.getOrderId());
  }

  @GetMapping(Constants.API.ORDER_BY_ID)
  public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
    return ResponseEntity.ok(orderService.getById(orderId));
  }
}
