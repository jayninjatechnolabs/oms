package com.example.oms.service.impl;

import com.example.oms.entity.OrderItem;
import com.example.oms.repository.OrderItemRepository;
import com.example.oms.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

  private final OrderItemRepository orderItemRepository;

  @Override
  @Transactional
  public List<OrderItem> saveAll(List<OrderItem> orderItems) {
    return orderItemRepository.saveAll(orderItems);
  }
}
