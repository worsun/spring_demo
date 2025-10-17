package com.worsun.spring_demo.query.service.impl;

import com.worsun.spring_demo.api.service.QueryDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService(version = "1.0.0", group = "query-group")
@Service
public class QueryDubboServiceImpl implements QueryDubboService {
  @Override
  public String sayHello() {
    return "Hello World from Query Service!";
  }

  @Override
  public String sayHelloTo(String name) {
    return "Hello " + name + " from Query Service!";
  }
}