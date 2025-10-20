package com.worsun.spring_demo.query.service.impl;

import com.worsun.spring_demo.api.dto.CustomerDto;
import com.worsun.spring_demo.api.service.QueryDubboService;
import com.worsun.spring_demo.query.domain.Customer;
import com.worsun.spring_demo.query.service.CustomerService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0", group = "query-group")
@Service
public class QueryDubboServiceImpl implements QueryDubboService {

  @Autowired
  private CustomerService customerService;

  @Override
  public String sayHello() {
    return "Hello World from Query Service!";
  }

  @Override
  public String sayHelloTo(String name) {
    return "Hello " + name + " from Query Service!";
  }

  @Override
  public CustomerDto getCustomer(Long id) {
    Customer customer = customerService.getCustomer(id);
    return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getCreatedAt());
  }

  @Override
  public List<CustomerDto> searchCustomers(String name, String email) {
    return customerService.searchCustomers(name, email)
        .stream()
        .map(customer -> new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(),
            customer.getCreatedAt()))
        .collect(Collectors.toList());
  }
}