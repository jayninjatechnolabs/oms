package com.example.oms.strategy;

import com.example.oms.entity.Order;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PricingContext {

  private final List<PricingStrategy> strategies;

  public PricingContext(List<PricingStrategy> strategies) {
    if (CollectionUtils.isEmpty(strategies)) {
      this.strategies = new ArrayList<>();
    } else {
      this.strategies = strategies;
    }
  }

  public void addStrategy(PricingStrategy strategy) {
    this.strategies.add(strategy);
  }

  public double calculateTotalDiscount(Order order) {
    double totalDiscount = 0.0;
    for (PricingStrategy strategy : strategies) {
      if (strategy.isApplicable(order)) {
        totalDiscount += strategy.applyDiscount(order);
      }
    }
    return totalDiscount;
  }
}
