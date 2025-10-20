package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.api.dto.CustomerDto;
import com.worsun.spring_demo.api.service.QueryDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DubboConsumerService {
  private static final Logger logger = LoggerFactory.getLogger(DubboConsumerService.class);

  @DubboReference(version = "1.0.0", group = "query-group", check = false, timeout = 30000, retries = 2)
  private QueryDubboService queryDubboService;

  public String callQueryService() {
    try {
      logger.info("Calling Dubbo service sayHello");
      String result = queryDubboService.sayHello();
      logger.info("Dubbo service sayHello returned: {}", result);
      return result;
    } catch (Exception e) {
      logger.error("Error calling Dubbo service sayHello", e);
      throw e;
    }
  }

  public String callQueryServiceWithName(String name) {
    try {
      logger.info("Calling Dubbo service sayHelloTo with name: {}", name);
      String result = queryDubboService.sayHelloTo(name);
      logger.info("Dubbo service sayHelloTo returned: {}", result);
      return result;
    } catch (Exception e) {
      logger.error("Error calling Dubbo service sayHelloTo", e);
      throw e;
    }
  }

  public CustomerDto getCustomer(Long id) {
    try {
      logger.info("Calling Dubbo service getCustomer with id: {}", id);
      CustomerDto result = queryDubboService.getCustomer(id);
      logger.info("Dubbo service getCustomer returned: {}", result);
      return result;
    } catch (Exception e) {
      logger.error("Error calling Dubbo service getCustomer", e);
      throw e;
    }
  }

  public List<CustomerDto> searchCustomers(String name, String email) {
    try {
      logger.info("Calling Dubbo service searchCustomers with name: {}, email: {}", name, email);
      List<CustomerDto> result = queryDubboService.searchCustomers(name, email);
      logger.info("Dubbo service searchCustomers returned {} items", result.size());
      return result;
    } catch (Exception e) {
      logger.error("Error calling Dubbo service searchCustomers", e);
      throw e;
    }
  }
}