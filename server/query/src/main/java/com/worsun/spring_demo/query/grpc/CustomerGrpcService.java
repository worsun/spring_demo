package com.worsun.spring_demo.query.grpc;

import com.worsun.spring_demo.proto.grpc.CustomerDto;
import com.worsun.spring_demo.proto.grpc.CustomerServiceGrpc;
import com.worsun.spring_demo.proto.grpc.GetCustomerRequest;
import com.worsun.spring_demo.proto.grpc.GetCustomerResponse;
import com.worsun.spring_demo.proto.grpc.SearchCustomersRequest;
import com.worsun.spring_demo.proto.grpc.SearchCustomersResponse;
import com.worsun.spring_demo.query.domain.Customer;
import com.worsun.spring_demo.query.service.CustomerService;
import com.worsun.spring_demo.query.service.CustomerService.CustomerNotFoundException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@GrpcService
public class CustomerGrpcService extends CustomerServiceGrpc.CustomerServiceImplBase {

  private static final Logger logger = LoggerFactory.getLogger(CustomerGrpcService.class);
  private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private final CustomerService customerService;

  public CustomerGrpcService(CustomerService customerService) {
    this.customerService = customerService;
  }

  @Override
  public void getCustomer(GetCustomerRequest request, StreamObserver<GetCustomerResponse> responseObserver) {
    logger.info("[QUERY-GRPC] Received gRPC request for customer ID: {}", request.getId());
    try {
      logger.info("[QUERY-GRPC] Calling customerService.getCustomer with ID: {}", request.getId());
      Customer customer = customerService.getCustomer(request.getId());
      logger.info("[QUERY-GRPC] Received customer from customerService: {}", customer);

      logger.info("[QUERY-GRPC] Mapping customer to DTO");
      CustomerDto customerDto = toDto(customer);
      logger.info("[QUERY-GRPC] Mapped customer DTO: {}", customerDto);

      logger.info("[QUERY-GRPC] Building GetCustomerResponse");
      GetCustomerResponse response = GetCustomerResponse.newBuilder()
          .setCustomer(customerDto)
          .build();
      logger.info("[QUERY-GRPC] Built response: {}", response);

      logger.info("[QUERY-GRPC] Sending response to client");
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      logger.info("[QUERY-GRPC] Completed gRPC request for customer ID: {}", request.getId());
    } catch (CustomerNotFoundException ex) {
      logger.warn("[QUERY-GRPC] Customer not found for ID: {}", request.getId());
      responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
    }
  }

  @Override
  public void searchCustomers(SearchCustomersRequest request,
      StreamObserver<SearchCustomersResponse> responseObserver) {
    logger.info("[QUERY-GRPC] Received gRPC search request with name: {}, email: {}", request.getName(),
        request.getEmail());
    logger.info("[QUERY-GRPC] Calling customerService.searchCustomers with name: {}, email: {}", request.getName(),
        request.getEmail());
    var customers = customerService.searchCustomers(request.getName(), request.getEmail());
    logger.info("[QUERY-GRPC] Received {} customers from customerService", customers.size());

    SearchCustomersResponse.Builder builder = SearchCustomersResponse.newBuilder();
    logger.info("[QUERY-GRPC] Mapping customers to DTOs");
    customers.stream().map(this::toDto).forEach(builder::addCustomers);
    logger.info("[QUERY-GRPC] Mapped {} customer DTOs", customers.size());

    SearchCustomersResponse response = builder.build();
    logger.info("[QUERY-GRPC] Built search response with {} customers", customers.size());

    logger.info("[QUERY-GRPC] Sending search response to client");
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    logger.info("[QUERY-GRPC] Completed gRPC search request");
  }

  private CustomerDto toDto(Customer customer) {
    logger.debug("[QUERY-GRPC] Mapping Customer to CustomerDto: {}", customer);
    String createdAt = customer.getCreatedAt() != null
        ? ISO_FORMATTER.format(customer.getCreatedAt().atOffset(ZoneOffset.UTC))
        : "";
    CustomerDto dto = CustomerDto.newBuilder()
        .setId(customer.getId())
        .setName(customer.getName())
        .setEmail(customer.getEmail())
        .setCreatedAt(createdAt)
        .build();
    logger.debug("[QUERY-GRPC] Mapped CustomerDto: {}", dto);
    return dto;
  }
}