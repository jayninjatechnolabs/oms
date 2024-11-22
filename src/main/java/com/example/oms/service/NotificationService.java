package com.example.oms.service;

import com.example.oms.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

  public void sendConfirmation(Order order) {
    // Simulate sending a notification
    log.info("Notification sent for order ID: {}", order.getOrderId());
  }
}
