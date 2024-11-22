package com.example.oms.service;

import com.example.oms.entity.Order;

public interface PaymentService {
  boolean process(Order order);
}
