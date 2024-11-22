package com.example.oms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import com.example.oms.service.impl.CustomerServiceImpl;
import com.example.oms.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerServiceImpl customerService;

  private ObjectMapper objectMapper;
  private Customer customer;
  private CustomerRequest customerRequest;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
    customer = new Customer();
    customer.setCustomerId(UUID.randomUUID().toString());
    customer.setName("John Doe");
    customer.setAccountNumber(123456789L);
    customer.setBankName("Bank of Example");
    customer.setOrderCount(0);

    customerRequest = new CustomerRequest();
    customerRequest.setName("John Doe");
    customerRequest.setAccountNumber(123456789L);
    customerRequest.setBankName("Bank of Example");
  }

  @Test
  public void testCreateCustomer() throws Exception {
    when(customerService.createAndSaveCustomer(any(CustomerRequest.class))).thenReturn(customer);

    mockMvc.perform(post(Constants.API.API_VERSION_V1 + Constants.API.CUSTOMERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.customerId").value(customer.getCustomerId()))
        .andExpect(jsonPath("$.name").value(customer.getName()))
        .andExpect(jsonPath("$.orderCount").value(customer.getOrderCount()));

    verify(customerService, times(1)).createAndSaveCustomer(any(CustomerRequest.class));
  }

  @Test
  public void testGetAllCustomers() throws Exception {
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);

    when(customerService.getAllCustomers()).thenReturn(customers);

    mockMvc.perform(get(Constants.API.API_VERSION_V1 + Constants.API.CUSTOMERS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].customerId").value(customer.getCustomerId()))
        .andExpect(jsonPath("$.[0].name").value(customer.getName()))
        .andExpect(jsonPath("$.[0].orderCount").value(customer.getOrderCount()));

    verify(customerService, times(1)).getAllCustomers();
  }

  @Test
  public void testGetAllCustomers_NoCustomers() throws Exception {
    when(customerService.getAllCustomers()).thenReturn(new ArrayList<>());

    mockMvc.perform(get(Constants.API.API_VERSION_V1 + Constants.API.CUSTOMERS))
        .andExpect(status().isNoContent());

    verify(customerService, times(1)).getAllCustomers();
  }
}
