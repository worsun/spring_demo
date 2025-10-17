package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.api.service.QueryDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class DubboConsumerService {

  @DubboReference(version = "1.0.0", group = "query-group", check = false)
  private QueryDubboService queryDubboService;

  public String callQueryService() {
    return queryDubboService.sayHello();
  }

  public String callQueryServiceWithName(String name) {
    return queryDubboService.sayHelloTo(name);
  }

}