package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.common.grpc.CustomerServiceGrpc;
import com.worsun.spring_demo.common.grpc.GetCustomerRequest;
import com.worsun.spring_demo.common.grpc.GetCustomerResponse;
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
        GetCustomerRequest request = GetCustomerRequest.newBuilder().setId(Long.parseLong(id)).build();
        try {
            return blockingStub.getCustomer(request);
        } catch (StatusRuntimeException ex) {
            log.error("gRPC query request failed for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    String.format("gRPC query service returned status %s", ex.getStatus()),
                    ex);
        }
    }
}