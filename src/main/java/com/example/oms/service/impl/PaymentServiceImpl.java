package com.example.oms.service.impl;

import com.example.oms.entity.Order;
import com.example.oms.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

  @Override
  public boolean process(Order order) {
    // Simulate processing payment
    log.info("Payment processed for order ID: {}", order.getOrderId());
    return true; // Simulate successful payment
  }
}
