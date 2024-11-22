package com.example.oms.service.impl;

import com.example.oms.dto.OrderStatus;
import com.example.oms.entity.Customer;
import com.example.oms.entity.Order;
import com.example.oms.entity.OrderItem;
import com.example.oms.repository.OrderRepository;
import com.example.oms.service.CustomerService;
import com.example.oms.service.NotificationService;
import com.example.oms.service.OrderItemService;
import com.example.oms.service.OrderService;
import com.example.oms.service.PaymentService;
import com.example.oms.strategy.PricingContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final PricingContext pricingContext;
  private final NotificationService notificationService;
  private final PaymentService paymentService;
  private final OrderItemService orderItemService;
  private final CustomerService customerService;

  @Override
  @Transactional
  public Order createOrder(List<OrderItem> items, Customer customer) {
    Order order = new Order(customer, items);
    order.setItems(orderItemService.saveAll(order.getItems()));
    double totalDiscount = pricingContext.calculateTotalDiscount(order);
    order.updateTotalPriceAndDiscount(totalDiscount);
    order = orderRepository.save(order);
    customer.incrementOrderCount();
    customerService.save(customer);
    sendNotification(order);
    processPayment(order);
    return order;
  }

  @Override
  public Order getById(String orderId) {
    return orderRepository.findById(orderId).orElse(null);
  }

  private void sendNotification(Order order) {
    notificationService.sendConfirmation(order);
  }

  private void processPayment(Order order) {
    if (paymentService.process(order)) {
      order.setStatus(OrderStatus.CONFIRMED);
      orderRepository.save(order);
    }
  }
}
