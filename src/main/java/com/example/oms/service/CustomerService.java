package com.example.oms.service;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import com.example.oms.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  public Customer createCustomer(CustomerRequest customerRequest) {
    Customer customer = new Customer();
    customer.setName(customerRequest.getName());
    customer.setAccountNumber(customerRequest.getAccountNumber());
    customer.setBankName(customerRequest.getBankName());
    customer.setOrderCount(0); // Initialize order count to 0
    return customerRepository.save(customer);
  }

  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  public Customer getById(String customerId) {
    return customerRepository.findById(customerId)
        .orElseThrow(() -> new RuntimeException("Invalid Customer Id"));
  }

  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }
}
