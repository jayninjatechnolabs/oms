package com.example.oms.entity;

import com.example.oms.dto.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String orderId;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  private LocalDateTime orderDate;
  private OrderStatus status;
  private double totalDiscount;
  private double totalPrice;
  private double totalPriceAfterDiscount;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<OrderItem> items;

  public Order(Customer customer, List<OrderItem> items) {
    this.customer = customer;
    this.orderDate = LocalDateTime.now();
    this.status = OrderStatus.PENDING;
    this.items = items;
    this.totalDiscount = 0.0;
    this.totalPrice = calculateTotalPrice();
    for (OrderItem item : items) {
      item.setOrder(this);
    }
  }

  public double calculateTotalPrice() {
    double total = 0.0;
    for (OrderItem item : items) {
      total += item.getSubtotal();
    }
    return total;
  }

  public void updateTotalPriceAndDiscount(double totalDiscount) {
    this.totalDiscount = totalDiscount;
    this.totalPriceAfterDiscount = calculateTotalPrice() - totalDiscount;
  }
}
