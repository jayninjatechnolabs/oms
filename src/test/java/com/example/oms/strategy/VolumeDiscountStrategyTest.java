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

public class VolumeDiscountStrategyTest {

  private VolumeDiscountStrategy volumeDiscountStrategy;
  private Order order;
  private Customer customer;
  private Product product;
  private OrderItem orderItem;

  @BeforeEach
  public void setUp() {
    volumeDiscountStrategy = new VolumeDiscountStrategy();

    customer = new Customer();

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
  public void testApplyDiscount_VolumeDiscount() {
    double discount = volumeDiscountStrategy.applyDiscount(order);

    assertEquals(150.0, discount, 0.001);
  }

  @Test
  public void testApplyDiscount_NoVolumeDiscount() {
    orderItem.setQuantity(5);
    orderItem.setSubtotal(500.0);
    order.setTotalPrice(500.0);
    order.setTotalPriceAfterDiscount(500.0);

    double discount = volumeDiscountStrategy.applyDiscount(order);

    assertEquals(0.0, discount, 0.001);
  }

  @Test
  public void testIsApplicable_VolumeDiscount() {
    boolean applicable = volumeDiscountStrategy.isApplicable(order);

    assertTrue(applicable);
  }

  @Test
  public void testIsApplicable_NoVolumeDiscount() {
    orderItem.setQuantity(5);
    orderItem.setSubtotal(500.0);
    order.setTotalPrice(500.0);
    order.setTotalPriceAfterDiscount(500.0);

    boolean applicable = volumeDiscountStrategy.isApplicable(order);

    assertFalse(applicable);
  }
}
