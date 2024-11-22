package com.example.oms.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.oms.dto.OrderStatus;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.entity.Product;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PricingContextTest {

  @Mock
  private LoyaltyDiscountStrategy loyaltyDiscountStrategy;

  @Mock
  private SeasonalDiscountStrategy seasonalDiscountStrategy;

  @Mock
  private VolumeDiscountStrategy volumeDiscountStrategy;

  @InjectMocks
  private PricingContext pricingContext;

  private Order order;
  private Customer customer;
  private Product product;
  private OrderItem orderItem;

  @BeforeEach
  public void setUp() {
    customer = new Customer();
    customer.setOrderCount(6);

    product = new Product();
    product.setBasePrice(100.0);

    orderItem = new OrderItem();
    orderItem.setProduct(product);
    orderItem.setQuantity(15);
    orderItem.setSubtotal(1500.0);

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    order = new Order(customer, orderItems);
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);
    order.setTotalDiscount(0.0);
    order.setTotalPrice(1500.0);
    order.setTotalPriceAfterDiscount(1500.0);
  }

  @Test
  public void testCalculateTotalDiscount_AllStrategiesApplicable() {
    when(loyaltyDiscountStrategy.isApplicable(any(Order.class))).thenReturn(true);
    when(loyaltyDiscountStrategy.applyDiscount(any(Order.class))).thenReturn(10.0);

    when(seasonalDiscountStrategy.isApplicable(any(Order.class))).thenReturn(true);
    when(seasonalDiscountStrategy.applyDiscount(any(Order.class))).thenReturn(30.0);

    when(volumeDiscountStrategy.isApplicable(any(Order.class))).thenReturn(true);
    when(volumeDiscountStrategy.applyDiscount(any(Order.class))).thenReturn(150.0);

    pricingContext = new PricingContext(
        List.of(loyaltyDiscountStrategy, seasonalDiscountStrategy, volumeDiscountStrategy));

    double totalDiscount = pricingContext.calculateTotalDiscount(order);

    assertEquals(190.0, totalDiscount, 0.001);

    verify(loyaltyDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(loyaltyDiscountStrategy, times(1)).applyDiscount(any(Order.class));

    verify(seasonalDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(seasonalDiscountStrategy, times(1)).applyDiscount(any(Order.class));

    verify(volumeDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(volumeDiscountStrategy, times(1)).applyDiscount(any(Order.class));
  }

  @Test
  public void testCalculateTotalDiscount_NoStrategiesApplicable() {
    when(loyaltyDiscountStrategy.isApplicable(any(Order.class))).thenReturn(false);

    when(seasonalDiscountStrategy.isApplicable(any(Order.class))).thenReturn(false);

    when(volumeDiscountStrategy.isApplicable(any(Order.class))).thenReturn(false);

    pricingContext = new PricingContext(
        List.of(loyaltyDiscountStrategy, seasonalDiscountStrategy, volumeDiscountStrategy));

    double totalDiscount = pricingContext.calculateTotalDiscount(order);

    assertEquals(0.0, totalDiscount, 0.001);

    verify(loyaltyDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(seasonalDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(volumeDiscountStrategy, times(1)).isApplicable(any(Order.class));
  }

  @Test
  public void testCalculateTotalDiscount_SomeStrategiesApplicable() {

    when(loyaltyDiscountStrategy.isApplicable(any(Order.class))).thenReturn(true);
    when(loyaltyDiscountStrategy.applyDiscount(any(Order.class))).thenReturn(10.0);

    when(seasonalDiscountStrategy.isApplicable(any(Order.class))).thenReturn(true);
    when(seasonalDiscountStrategy.applyDiscount(any(Order.class))).thenReturn(30.0);

    when(volumeDiscountStrategy.isApplicable(any(Order.class))).thenReturn(false);

    pricingContext = new PricingContext(
        List.of(loyaltyDiscountStrategy, seasonalDiscountStrategy, volumeDiscountStrategy));

    double totalDiscount = pricingContext.calculateTotalDiscount(order);

    assertEquals(40.0, totalDiscount, 0.001);

    verify(loyaltyDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(loyaltyDiscountStrategy, times(1)).applyDiscount(any(Order.class));

    verify(seasonalDiscountStrategy, times(1)).isApplicable(any(Order.class));
    verify(seasonalDiscountStrategy, times(1)).applyDiscount(any(Order.class));

    verify(volumeDiscountStrategy, times(1)).isApplicable(any(Order.class));
  }
}
