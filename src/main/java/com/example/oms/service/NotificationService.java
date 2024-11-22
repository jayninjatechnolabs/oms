package com.example.oms.service;

import com.example.oms.entity.Order;

public interface NotificationService {
  void sendConfirmation(Order order);
}
