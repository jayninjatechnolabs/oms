package com.example.oms.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class LoyaltyDiscountStrategyTest {

  private LoyaltyDiscountStrategy loyaltyDiscountStrategy;
  private Order order;
  private Customer customer;
  private Product product;
  private OrderItem orderItem;

  @BeforeEach
  public void setUp() {
    loyaltyDiscountStrategy = new LoyaltyDiscountStrategy();

    customer = new Customer();
    customer.setOrderCount(6);

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
  public void testApplyDiscount_LoyalCustomer() {
    double discount = loyaltyDiscountStrategy.applyDiscount(order);

    assertEquals(10.0, discount, 0.001);
  }

  @Test
  public void testIsApplicable_LoyalCustomer() {
    boolean applicable = loyaltyDiscountStrategy.isApplicable(order);

    assertTrue(applicable);
  }

  @Test
  public void testIsApplicable_NotLoyalCustomer() {
    customer.setOrderCount(4);
    boolean applicable = loyaltyDiscountStrategy.isApplicable(order);

    assertFalse(applicable);
  }
}
