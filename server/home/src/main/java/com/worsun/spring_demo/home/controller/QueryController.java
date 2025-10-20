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
        logger.info("[HOME] Received HTTP query request for ID: {}", id);
        logger.info("[HOME] Calling gatewayService.queryViaHttp with ID: {}", id);
        QueryResult result = gatewayService.queryViaHttp(id);
        logger.info("[HOME] Received result from gatewayService.queryViaHttp: {}", result);
        logger.info("[HOME] Returning HTTP query result for ID: {}", id);
        return result;
    }

    @GetMapping("/grpc/{id}")
    public QueryResult queryViaGrpc(@PathVariable("id") String id) {
        logger.info("[HOME] Received gRPC query request for ID: {}", id);
        logger.info("[HOME] Calling gatewayService.queryViaGrpc with ID: {}", id);
        QueryResult result = gatewayService.queryViaGrpc(id);
        logger.info("[HOME] Received result from gatewayService.queryViaGrpc: {}", result);
        logger.info("[HOME] Returning gRPC query result for ID: {}", id);
        return result;
    }

    @GetMapping("/dubbo-test")
    public String dubboTest() {
        try {
            logger.info("[HOME] Received request for dubboTest");
            logger.info("[HOME] Calling dubboConsumerService.callQueryService");
            String result = dubboConsumerService.callQueryService();
            logger.info("[HOME] Received result from dubboConsumerService.callQueryService: {}", result);
            logger.info("[HOME] Returning dubboTest result");
            return result;
        } catch (Exception e) {
            logger.error("[HOME] Error in dubboTest", e);
            throw e;
        }
    }

    @GetMapping("/dubbo-test/{name}")
    public String dubboTestWithName(@PathVariable String name) {
        try {
            logger.info("[HOME] Received request for dubboTestWithName with name: {}", name);
            logger.info("[HOME] Calling dubboConsumerService.callQueryServiceWithName with name: {}", name);
            String result = dubboConsumerService.callQueryServiceWithName(name);
            logger.info("[HOME] Received result from dubboConsumerService.callQueryServiceWithName: {}", result);
            logger.info("[HOME] Returning dubboTestWithName result for name: {}", name);
            return result;
        } catch (Exception e) {
            logger.error("[HOME] Error in dubboTestWithName", e);
            throw e;
        }
    }

    @GetMapping("/dubbo/{id}")
    public CustomerDto queryViaDubbo(@PathVariable("id") Long id) {
        try {
            logger.info("[HOME] Received request for queryViaDubbo with id: {}", id);
            logger.info("[HOME] Calling dubboConsumerService.getCustomer with id: {}", id);
            CustomerDto result = dubboConsumerService.getCustomer(id);
            logger.info("[HOME] Received result from dubboConsumerService.getCustomer: {}", result);
            logger.info("[HOME] Returning Dubbo query result for id: {}", id);
            return result;
        } catch (Exception e) {
            logger.error("[HOME] Error in queryViaDubbo for id: " + id, e);
            throw new RuntimeException("Error calling Dubbo service", e);
        }
    }

    @GetMapping("/dubbo/search")
    public List<CustomerDto> searchViaDubbo(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        try {
            logger.info("[HOME] Received request for searchViaDubbo with name: {}, email: {}", name, email);
            logger.info("[HOME] Calling dubboConsumerService.searchCustomers with name: {}, email: {}", name, email);
            List<CustomerDto> result = dubboConsumerService.searchCustomers(name, email);
            logger.info("[HOME] Received result from dubboConsumerService.searchCustomers, size: {}", result.size());
            logger.info("[HOME] Returning Dubbo search result with size: {}", result.size());
            return result;
        } catch (Exception e) {
            logger.error("[HOME] Error in searchViaDubbo", e);
            throw new RuntimeException("Error calling Dubbo search service", e);
        }
    }
}