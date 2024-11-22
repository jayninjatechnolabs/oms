package com.example.oms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
  private List<ItemRequest> items;
  private String customerId;

  @JsonIgnore
  public List<String> getProductIds() {
    return this.items.stream().map(ItemRequest::getProductId).toList();
  }
}
