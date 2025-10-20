package com.worsun.spring_demo.api.service;

import com.worsun.spring_demo.api.dto.CustomerDto;
import java.util.List;

public interface QueryDubboService {
  String sayHello();

  String sayHelloTo(String name);

  CustomerDto getCustomer(Long id);

  List<CustomerDto> searchCustomers(String name, String email);
}