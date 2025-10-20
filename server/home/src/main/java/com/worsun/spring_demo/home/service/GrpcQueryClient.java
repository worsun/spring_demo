package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.proto.grpc.CustomerServiceGrpc;
import com.worsun.spring_demo.proto.grpc.GetCustomerRequest;
import com.worsun.spring_demo.proto.grpc.GetCustomerResponse;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class GrpcQueryClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcQueryClient.class);

    private final CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub;

    public GrpcQueryClient(CustomerServiceGrpc.CustomerServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public GetCustomerResponse fetchRecord(String id) {
        log.info("[HOME-GRPC-CLIENT] Starting gRPC request for customer ID: {}", id);
        log.info("[HOME-GRPC-CLIENT] Building GetCustomerRequest with ID: {}", id);
        GetCustomerRequest request = GetCustomerRequest.newBuilder().setId(Long.parseLong(id)).build();
        log.info("[HOME-GRPC-CLIENT] Built request: {}", request);

        try {
            log.info("[HOME-GRPC-CLIENT] Sending gRPC request to blockingStub.getCustomer");
            GetCustomerResponse response = blockingStub.getCustomer(request);
            log.info("[HOME-GRPC-CLIENT] Received gRPC response: {}", response);
            log.info("[HOME-GRPC-CLIENT] Completed gRPC request for customer ID: {}", id);
            return response;
        } catch (StatusRuntimeException ex) {
            log.error("[HOME-GRPC-CLIENT] gRPC query request failed for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }
}