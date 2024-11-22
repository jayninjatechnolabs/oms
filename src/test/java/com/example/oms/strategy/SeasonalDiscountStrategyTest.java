package com.example.oms.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.oms.dto.OrderStatus;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.entity.Product;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SeasonalDiscountStrategyTest {

  @Mock
  private Clock clock;

  @InjectMocks
  private SeasonalDiscountStrategy seasonalDiscountStrategy;

  private Order order;
  private Customer customer;
  private Product product;
  private OrderItem orderItem;

  @BeforeEach
  public void setUp() {
    customer = new Customer();

    product = new Product();
    product.setBasePrice(100.0);

    orderItem = new OrderItem();
    orderItem.setProduct(product);
    orderItem.setQuantity(2);
    orderItem.setSubtotal(200.0);

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    order = new Order(customer, orderItems);
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);
    order.setTotalDiscount(0.0);
    order.setTotalPrice(200.0);
    order.setTotalPriceAfterDiscount(200.0);
  }

  @Test
  public void testApplyDiscount_Seasonal() {
    double discount = seasonalDiscountStrategy.applyDiscount(order);

    assertEquals(30.0, discount, 0.001);
  }

  @Test
  public void testIsApplicable_Seasonal() {
    LocalDate fixedDate = LocalDate.of(2023, 12, 25);
    when(clock.instant()).thenReturn(fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    when(clock.getZone()).thenReturn(ZoneId.systemDefault());

    boolean applicable = seasonalDiscountStrategy.isApplicable(order);

    assertTrue(applicable);
  }

  @Test
  public void testIsApplicable_NotSeasonal() {
    LocalDate fixedDate = LocalDate.of(2023, 11, 25);
    when(clock.instant()).thenReturn(fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    when(clock.getZone()).thenReturn(ZoneId.systemDefault());

    boolean applicable = seasonalDiscountStrategy.isApplicable(order);

    assertFalse(applicable);
  }
}
