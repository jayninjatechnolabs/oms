package com.example.oms.service;

import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import java.util.List;

public interface OrderService {
  Order createOrder(List<OrderItem> items, Customer customer);

  Order getById(String orderId);
}
