package com.example.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.oms.dto.CustomerRequest;
import com.example.oms.entity.Customer;
import com.example.oms.repository.CustomerRepository;
import com.example.oms.service.impl.CustomerServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerServiceImpl customerService;

  private Customer customer;
  private CustomerRequest customerRequest;

  @BeforeEach
  public void setUp() {
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
  public void testCreateAndSaveCustomer() {
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    Customer createdCustomer = customerService.createAndSaveCustomer(customerRequest);

    assertNotNull(createdCustomer);
    assertEquals(customer.getName(), createdCustomer.getName());
    assertEquals(customer.getAccountNumber(), createdCustomer.getAccountNumber());
    assertEquals(customer.getBankName(), createdCustomer.getBankName());
    assertEquals(customer.getOrderCount(), createdCustomer.getOrderCount());

    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  public void testGetAllCustomers() {
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);

    when(customerRepository.findAll()).thenReturn(customers);

    List<Customer> retrievedCustomers = customerService.getAllCustomers();

    assertNotNull(retrievedCustomers);
    assertEquals(1, retrievedCustomers.size());
    assertEquals(customer.getName(), retrievedCustomers.get(0).getName());

    verify(customerRepository, times(1)).findAll();
  }

  @Test
  public void testGetById() {
    when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

    Customer retrievedCustomer = customerService.getById(customer.getCustomerId());

    assertNotNull(retrievedCustomer);
    assertEquals(customer.getCustomerId(), retrievedCustomer.getCustomerId());
    assertEquals(customer.getName(), retrievedCustomer.getName());

    verify(customerRepository, times(1)).findById(anyString());
  }

  @Test
  public void testGetById_NotFound() {
    when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> customerService.getById(UUID.randomUUID().toString()));

    verify(customerRepository, times(1)).findById(anyString());
  }

  @Test
  public void testSave() {
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    Customer savedCustomer = customerService.save(customer);

    assertNotNull(savedCustomer);
    assertEquals(customer.getCustomerId(), savedCustomer.getCustomerId());
    assertEquals(customer.getName(), savedCustomer.getName());

    verify(customerRepository, times(1)).save(any(Customer.class));
  }
}
