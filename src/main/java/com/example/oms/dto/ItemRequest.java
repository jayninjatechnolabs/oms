package com.example.oms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {
  private String productId;
  private int quantity;
}
