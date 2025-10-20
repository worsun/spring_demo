package com.worsun.spring_demo.home.controller;

import com.worsun.spring_demo.api.dto.CustomerDto;
import com.worsun.spring_demo.home.model.QueryResult;
import com.worsun.spring_demo.home.service.DubboConsumerService;
import com.worsun.spring_demo.home.service.QueryGatewayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    private final QueryGatewayService gatewayService;
    @Autowired
    private DubboConsumerService dubboConsumerService;

    public QueryController(QueryGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/http/{id}")
    public QueryResult queryViaHttp(@PathVariable("id") String id) {
        logger.info("Received HTTP query request for ID: {}", id);
        return gatewayService.queryViaHttp(id);
    }

    @GetMapping("/grpc/{id}")
    public QueryResult queryViaGrpc(@PathVariable("id") String id) {
        logger.info("Received gRPC query request for ID: {}", id);
        return gatewayService.queryViaGrpc(id);
    }

    @GetMapping("/dubbo-test")
    public String dubboTest() {
        try {
            logger.info("Received request for dubboTest");
            String result = dubboConsumerService.callQueryService();
            logger.info("Dubbo test result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error in dubboTest", e);
            throw e;
        }
    }

    @GetMapping("/dubbo-test/{name}")
    public String dubboTestWithName(@PathVariable String name) {
        try {
            logger.info("Received request for dubboTestWithName with name: {}", name);
            String result = dubboConsumerService.callQueryServiceWithName(name);
            logger.info("Dubbo test with name result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error in dubboTestWithName", e);
            throw e;
        }
    }

    @GetMapping("/dubbo/{id}")
    public CustomerDto queryViaDubbo(@PathVariable("id") Long id) {
        try {
            logger.info("Received request for queryViaDubbo with id: {}", id);
            CustomerDto result = dubboConsumerService.getCustomer(id);
            logger.info("Dubbo query result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error in queryViaDubbo for id: " + id, e);
            throw new RuntimeException("Error calling Dubbo service", e);
        }
    }

    @GetMapping("/dubbo/search")
    public List<CustomerDto> searchViaDubbo(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        try {
            logger.info("Received request for searchViaDubbo with name: {}, email: {}", name, email);
            List<CustomerDto> result = dubboConsumerService.searchCustomers(name, email);
            logger.info("Dubbo search result size: {}", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error in searchViaDubbo", e);
            throw new RuntimeException("Error calling Dubbo search service", e);
        }
    }
}