package com.example.oms.strategy;

import com.example.oms.entity.Order;
import java.time.Clock;
import java.time.LocalDate;
import java.time.MonthDay;

public class SeasonalDiscountStrategy implements PricingStrategy {

  private static final double SEASONAL_DISCOUNT_RATE = 0.15;
  private final Clock clock;

  public SeasonalDiscountStrategy() {
    this(Clock.systemDefaultZone());
  }

  public SeasonalDiscountStrategy(Clock clock) {
    this.clock = clock;
  }

  @Override
  public double applyDiscount(Order order) {
    double basePrice = order.calculateTotalPrice();
    return basePrice * SEASONAL_DISCOUNT_RATE;
  }

  @Override
  public boolean isApplicable(Order order) {
    LocalDate now = LocalDate.now(clock);
    MonthDay today = MonthDay.from(now);
    return today.isAfter(MonthDay.of(12, 20)) && today.isBefore(MonthDay.of(12, 31));
  }
}
