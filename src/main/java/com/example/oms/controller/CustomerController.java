package com.example.oms.controller;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import com.example.oms.service.CustomerService;
import com.example.oms.util.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API.API_VERSION_V1 + Constants.API.CUSTOMERS)
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest customerRequest) {
    Customer customer = customerService.createAndSaveCustomer(customerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(customer);
  }

  @GetMapping
  public ResponseEntity<List<Customer>> getAllCustomers() {
    List<Customer> allCustomers = customerService.getAllCustomers();
    if (allCustomers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(allCustomers);
  }
}
