package com.example.oms.strategy;

import com.example.oms.entity.Order;

public interface PricingStrategy {
  double applyDiscount(Order order);

  boolean isApplicable(Order order);
}
