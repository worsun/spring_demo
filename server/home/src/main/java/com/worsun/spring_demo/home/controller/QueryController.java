package com.worsun.spring_demo.home.controller;

import com.worsun.spring_demo.home.model.QueryResult;
import com.worsun.spring_demo.home.service.DubboConsumerService;
import com.worsun.spring_demo.home.service.QueryGatewayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryGatewayService gatewayService;
    @Autowired
    private DubboConsumerService dubboConsumerService;

    public QueryController(QueryGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/http/{id}")
    public QueryResult queryViaHttp(@PathVariable("id") String id) {
        return gatewayService.queryViaHttp(id);
    }

    @GetMapping("/grpc/{id}")
    public QueryResult queryViaGrpc(@PathVariable("id") String id) {
        return gatewayService.queryViaGrpc(id);
    }

    @GetMapping("/dubbo-test")
    public String dubboTest() {
        return dubboConsumerService.callQueryService();
    }

    @GetMapping("/dubbo-test/{name}")
    public String dubboTestWithName(@PathVariable String name) {
        return dubboConsumerService.callQueryServiceWithName(name);
    }
}