package com.example.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.oms.dto.OrderStatus;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.entity.Product;
import com.example.oms.repository.OrderRepository;
import com.example.oms.strategy.PricingContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock
  private PricingContext pricingContext;

  @Mock
  private NotificationService notificationService;

  @Mock
  private PaymentService paymentService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderItemService orderItemService;

  @Mock
  private CustomerService customerService;

  @InjectMocks
  private OrderService orderService;

  private Customer customer;
  private Product product;
  private OrderItem orderItem;
  private List<OrderItem> orderItems;
  private Order order;

  @BeforeEach
  public void setUp() {
    customer = new Customer();
    customer.setCustomerId(UUID.randomUUID().toString());

    product = new Product();
    product.setProductId(UUID.randomUUID().toString());
    product.setName("Test Product");
    product.setBasePrice(100.0);

    orderItem = new OrderItem();
    orderItem.setOrderItemId(UUID.randomUUID().toString());
    orderItem.setProduct(product);
    orderItem.setQuantity(2);
    orderItem.setSubtotal(200.0);

    orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    order = new Order(customer, orderItems);
    order.setOrderId(UUID.randomUUID().toString());
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);
    order.setTotalDiscount(0.0);
    order.setTotalPrice(200.0);
    order.setTotalPriceAfterDiscount(200.0);
  }

  @Test
  @Transactional
  public void testCreateOrder() {
    when(pricingContext.calculateTotalDiscount(any(Order.class))).thenReturn(0.0);
    when(orderItemService.saveAll(anyList())).thenReturn(orderItems);
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(customerService.save(any(Customer.class))).thenReturn(customer);
    when(paymentService.process(any(Order.class))).thenReturn(true);

    Order createdOrder = orderService.createOrder(orderItems, customer);

    assertNotNull(createdOrder);
    assertEquals(order.getOrderId(), createdOrder.getOrderId());
    assertEquals(order.getOrderDate(), createdOrder.getOrderDate());
    assertEquals(order.getStatus(), createdOrder.getStatus());
    assertEquals(order.getTotalDiscount(), createdOrder.getTotalDiscount());
    assertEquals(order.getTotalPrice(), createdOrder.getTotalPrice());
    assertEquals(order.getTotalPriceAfterDiscount(), createdOrder.getTotalPriceAfterDiscount());
    assertEquals(order.getItems().size(), createdOrder.getItems().size());
    assertEquals(order.getItems().get(0).getOrderItemId(),
        createdOrder.getItems().get(0).getOrderItemId());
    assertEquals(order.getItems().get(0).getProduct().getProductId(),
        createdOrder.getItems().get(0).getProduct().getProductId());
    assertEquals(order.getItems().get(0).getQuantity(),
        createdOrder.getItems().get(0).getQuantity());
    assertEquals(order.getItems().get(0).getSubtotal(),
        createdOrder.getItems().get(0).getSubtotal());

    verify(pricingContext, times(1)).calculateTotalDiscount(any(Order.class));
    verify(orderItemService, times(1)).saveAll(anyList());
    verify(orderRepository, times(2)).save(any(Order.class));
    verify(customerService, times(1)).save(any(Customer.class));
    verify(notificationService, times(1)).sendConfirmation(any(Order.class));
    verify(paymentService, times(1)).process(any(Order.class));
  }

  @Test
  public void testGetById() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

    Order retrievedOrder = orderService.getById(order.getOrderId());

    assertNotNull(retrievedOrder);
    assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
    assertEquals(order.getOrderDate(), retrievedOrder.getOrderDate());
    assertEquals(order.getStatus(), retrievedOrder.getStatus());
    assertEquals(order.getTotalDiscount(), retrievedOrder.getTotalDiscount());
    assertEquals(order.getTotalPrice(), retrievedOrder.getTotalPrice());
    assertEquals(order.getTotalPriceAfterDiscount(), retrievedOrder.getTotalPriceAfterDiscount());
    assertEquals(order.getItems().size(), retrievedOrder.getItems().size());
    assertEquals(order.getItems().get(0).getOrderItemId(),
        retrievedOrder.getItems().get(0).getOrderItemId());
    assertEquals(order.getItems().get(0).getProduct().getProductId(),
        retrievedOrder.getItems().get(0).getProduct().getProductId());
    assertEquals(order.getItems().get(0).getQuantity(),
        retrievedOrder.getItems().get(0).getQuantity());
    assertEquals(order.getItems().get(0).getSubtotal(),
        retrievedOrder.getItems().get(0).getSubtotal());

    verify(orderRepository, times(1)).findById(anyString());
  }

  @Test
  public void testGetById_NotFound() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

    Order retrievedOrder = orderService.getById(UUID.randomUUID().toString());

    assertNull(retrievedOrder);

    verify(orderRepository, times(1)).findById(anyString());
  }
}
