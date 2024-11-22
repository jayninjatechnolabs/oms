package com.example.oms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String customerId;
  private String name;
  private int orderCount;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Long accountNumber;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String bankName;

  public void incrementOrderCount() {
    this.orderCount++;
  }
}
