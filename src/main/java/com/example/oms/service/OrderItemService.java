package com.example.oms.service;

import com.example.oms.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
  List<OrderItem> saveAll(List<OrderItem> orderItems);
}
