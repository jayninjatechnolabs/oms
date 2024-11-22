package com.example.oms.service.impl;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import com.example.oms.repository.CustomerRepository;
import com.example.oms.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  @Override
  @Transactional
  public Customer createAndSaveCustomer(CustomerRequest customerRequest) {
    Customer customer = new Customer();
    customer.setName(customerRequest.getName());
    customer.setAccountNumber(customerRequest.getAccountNumber());
    customer.setBankName(customerRequest.getBankName());
    customer.setOrderCount(0); // Initialize order count to 0
    return customerRepository.save(customer);
  }

  @Override
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  @Override
  public Customer getById(String customerId) {
    return customerRepository.findById(customerId)
        .orElseThrow(() -> new RuntimeException("Invalid Customer Id"));
  }

  @Override
  @Transactional
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }
}
