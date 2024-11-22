package com.example.oms.service.impl;

import com.example.oms.entity.Order;
import com.example.oms.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

  @Override
  public void sendConfirmation(Order order) {
    // Simulate sending a notification
    log.info("Notification sent for order ID: {}", order.getOrderId());
  }
}
