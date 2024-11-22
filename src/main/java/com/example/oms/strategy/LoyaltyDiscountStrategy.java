package com.example.oms.strategy;

import com.example.oms.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyDiscountStrategy implements PricingStrategy {

  private static final double LOYALTY_DISCOUNT_RATE = 0.05;
  private static final int LOYALTY_THRESHOLD = 5;

  @Override
  public double applyDiscount(Order order) {
    double basePrice = order.calculateTotalPrice();
    return basePrice * LOYALTY_DISCOUNT_RATE;
  }

  @Override
  public boolean isApplicable(Order order) {
    return order.getCustomer().getOrderCount() > LOYALTY_THRESHOLD;
  }
}
