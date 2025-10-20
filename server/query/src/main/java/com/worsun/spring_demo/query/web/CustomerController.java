package com.worsun.spring_demo.query.web;

import com.worsun.spring_demo.query.domain.Customer;
import com.worsun.spring_demo.query.dto.CustomerResponse;
import com.worsun.spring_demo.query.dto.CustomerSearchRequest;
import com.worsun.spring_demo.query.service.CustomerService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") Long id) {
    logger.info("[QUERY-HTTP] Received request for customer ID: {}", id);
    logger.info("[QUERY-HTTP] Calling customerService.getCustomer with ID: {}", id);
    Customer customer = customerService.getCustomer(id);
    logger.info("[QUERY-HTTP] Received customer from customerService: {}", customer);

    logger.info("[QUERY-HTTP] Mapping customer to CustomerResponse");
    CustomerResponse response = toResponse(customer);
    logger.info("[QUERY-HTTP] Mapped response: {}", response);
    logger.info("[QUERY-HTTP] Returning response for customer ID: {}", id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> searchCustomers(@Valid CustomerSearchRequest request) {
    logger.info("[QUERY-HTTP] Received search request with name: {}, email: {}", request.getName(), request.getEmail());
    logger.info("[QUERY-HTTP] Calling customerService.searchCustomers with name: {}, email: {}", request.getName(),
        request.getEmail());
    List<CustomerResponse> responses = customerService.searchCustomers(request.getName(), request.getEmail())
        .stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
    logger.info("[QUERY-HTTP] Received {} customers from customerService", responses.size());
    logger.info("[QUERY-HTTP] Returning search response with {} customers", responses.size());
    return ResponseEntity.ok(responses);
  }

  private CustomerResponse toResponse(Customer customer) {
    logger.debug("[QUERY-HTTP] Mapping Customer to CustomerResponse: {}", customer);
    CustomerResponse response = new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(),
        customer.getCreatedAt());
    logger.debug("[QUERY-HTTP] Mapped CustomerResponse: {}", response);
    return response;
  }
}