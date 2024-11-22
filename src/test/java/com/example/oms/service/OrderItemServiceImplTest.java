package com.example.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.oms.entity.OrderItem;
import com.example.oms.repository.OrderItemRepository;
import com.example.oms.service.impl.OrderItemServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceImplTest {

  @Mock
  private OrderItemRepository orderItemRepository;

  @InjectMocks
  private OrderItemServiceImpl orderItemService;

  private OrderItem orderItem;
  private List<OrderItem> orderItems;

  @BeforeEach
  public void setUp() {
    orderItem = new OrderItem();
    orderItem.setOrderItemId(UUID.randomUUID().toString());
    orderItem.setQuantity(2);
    orderItem.setSubtotal(200.0);

    orderItems = new ArrayList<>();
    orderItems.add(orderItem);
  }

  @Test
  public void testSaveAll() {
    when(orderItemRepository.saveAll(anyList())).thenReturn(orderItems);

    List<OrderItem> savedOrderItems = orderItemService.saveAll(orderItems);

    assertNotNull(savedOrderItems);
    assertEquals(1, savedOrderItems.size());
    assertEquals(orderItem.getOrderItemId(), savedOrderItems.get(0).getOrderItemId());
    assertEquals(orderItem.getQuantity(), savedOrderItems.get(0).getQuantity());
    assertEquals(orderItem.getSubtotal(), savedOrderItems.get(0).getSubtotal());

    verify(orderItemRepository, times(1)).saveAll(anyList());
  }
}
