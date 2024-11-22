package com.example.oms.service;

import com.example.oms.entity.OrderItem;
import com.example.oms.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

  private final OrderItemRepository orderItemRepository;

  public List<OrderItem> saveAll(List<OrderItem> orderItems) {
    return orderItemRepository.saveAll(orderItems);
  }
}
