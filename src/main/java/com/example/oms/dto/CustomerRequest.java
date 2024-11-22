package com.example.oms.dto;

import lombok.Data;

@Data
public class CustomerRequest {
  private String name;
  private Long accountNumber;
  private String bankName;
}
