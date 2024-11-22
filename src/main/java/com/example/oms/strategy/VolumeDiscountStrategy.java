package com.example.oms.strategy;

import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class VolumeDiscountStrategy implements PricingStrategy {

  private static final double VOLUME_DISCOUNT_RATE = 0.10;
  private static final int VOLUME_THRESHOLD = 10;

  @Override
  public double applyDiscount(Order order) {
    double totalDiscount = 0.0;
    for (OrderItem item : order.getItems()) {
      if (item.getQuantity() > VOLUME_THRESHOLD) {
        totalDiscount += item.getSubtotal() * VOLUME_DISCOUNT_RATE;
      }
    }
    return totalDiscount;
  }

  @Override
  public boolean isApplicable(Order order) {
    for (OrderItem item : order.getItems()) {
      if (item.getQuantity() > VOLUME_THRESHOLD) {
        return true;
      }
    }
    return false;
  }
}
