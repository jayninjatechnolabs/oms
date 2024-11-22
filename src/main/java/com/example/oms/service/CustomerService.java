package com.example.oms.service;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import java.util.List;

public interface CustomerService {
  Customer createAndSaveCustomer(CustomerRequest customerRequest);

  List<Customer> getAllCustomers();

  Customer getById(String customerId);

  Customer save(Customer customer);
}
