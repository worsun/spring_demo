package com.worsun.spring_demo.query.service;

import com.worsun.spring_demo.query.domain.Customer;
import com.worsun.spring_demo.query.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  private final CustomerMapper customerMapper;

  public CustomerService(CustomerMapper customerMapper) {
    this.customerMapper = customerMapper;
  }

  public Customer getCustomer(Long id) {
    logger.info("[QUERY-SERVICE] getCustomer called with id: {}", id);
    logger.info("[QUERY-SERVICE] Calling customerMapper.findById with id: {}", id);
    Customer customer = customerMapper.findById(id)
        .orElseThrow(() -> {
          logger.warn("[QUERY-SERVICE] Customer not found for id: {}", id);
          return new CustomerNotFoundException(id);
        });
    logger.info("[QUERY-SERVICE] Found customer: {}", customer);
    logger.info("[QUERY-SERVICE] getCustomer returning customer for id: {}", id);
    return customer;
  }

  public List<Customer> searchCustomers(String name, String email) {
    logger.info("[QUERY-SERVICE] searchCustomers called with name: {}, email: {}", name, email);
    String normalizedName = StringUtils.hasText(name) ? name.trim() : null;
    String normalizedEmail = StringUtils.hasText(email) ? email.trim() : null;
    logger.info("[QUERY-SERVICE] Normalized search parameters - name: {}, email: {}", normalizedName, normalizedEmail);

    logger.info("[QUERY-SERVICE] Calling customerMapper.searchCustomers with name: {}, email: {}", normalizedName,
        normalizedEmail);
    List<Customer> customers = customerMapper.searchCustomers(normalizedName, normalizedEmail);
    logger.info("[QUERY-SERVICE] Found {} customers", customers.size());
    logger.info("[QUERY-SERVICE] searchCustomers returning list with {} customers", customers.size());
    return customers;
  }

  public static class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
      super("Customer not found for id=" + id);
    }
  }
}