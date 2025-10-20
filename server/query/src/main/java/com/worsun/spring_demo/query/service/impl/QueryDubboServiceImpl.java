package com.worsun.spring_demo.query.service.impl;

import com.worsun.spring_demo.api.dto.CustomerDto;
import com.worsun.spring_demo.api.service.QueryDubboService;
import com.worsun.spring_demo.query.domain.Customer;
import com.worsun.spring_demo.query.service.CustomerService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0", group = "query-group")
@Service
public class QueryDubboServiceImpl implements QueryDubboService {

  private static final Logger logger = LoggerFactory.getLogger(QueryDubboServiceImpl.class);

  @Autowired
  private CustomerService customerService;

  @Override
  public String sayHello() {
    logger.info("[QUERY-DUBBO] sayHello called");
    String result = "Hello World from Query Service!";
    logger.info("[QUERY-DUBBO] sayHello returning: {}", result);
    return result;
  }

  @Override
  public String sayHelloTo(String name) {
    logger.info("[QUERY-DUBBO] sayHelloTo called with name: {}", name);
    String result = "Hello " + name + " from Query Service!";
    logger.info("[QUERY-DUBBO] sayHelloTo returning: {}", result);
    return result;
  }

  @Override
  public CustomerDto getCustomer(Long id) {
    logger.info("[QUERY-DUBBO] getCustomer called with id: {}", id);
    logger.info("[QUERY-DUBBO] Calling customerService.getCustomer with id: {}", id);
    Customer customer = customerService.getCustomer(id);
    logger.info("[QUERY-DUBBO] Received customer from customerService: {}", customer);

    logger.info("[QUERY-DUBBO] Mapping customer to CustomerDto");
    CustomerDto dto = new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(),
        customer.getCreatedAt());
    logger.info("[QUERY-DUBBO] Mapped CustomerDto: {}", dto);
    logger.info("[QUERY-DUBBO] getCustomer returning CustomerDto for id: {}", id);
    return dto;
  }

  @Override
  public List<CustomerDto> searchCustomers(String name, String email) {
    logger.info("[QUERY-DUBBO] searchCustomers called with name: {}, email: {}", name, email);
    logger.info("[QUERY-DUBBO] Calling customerService.searchCustomers with name: {}, email: {}", name, email);
    List<Customer> customers = customerService.searchCustomers(name, email);
    logger.info("[QUERY-DUBBO] Received {} customers from customerService", customers.size());

    logger.info("[QUERY-DUBBO] Mapping customers to CustomerDtos");
    List<CustomerDto> dtos = customers.stream()
        .map(customer -> new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(),
            customer.getCreatedAt()))
        .collect(Collectors.toList());
    logger.info("[QUERY-DUBBO] Mapped {} CustomerDtos", dtos.size());
    logger.info("[QUERY-DUBBO] searchCustomers returning list with {} customers", dtos.size());
    return dtos;
  }
}