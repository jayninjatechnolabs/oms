package com.example.oms.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  @UtilityClass
  public class API {
    public final String API_VERSION_V1 = "/v1";
    public final String CUSTOMERS = "/customers";
    public final String PRODUCTS = "/products";
    public final String ORDERS = "/orders";
    public final String ORDER_BY_ID = "/{orderId}";

  }
}
