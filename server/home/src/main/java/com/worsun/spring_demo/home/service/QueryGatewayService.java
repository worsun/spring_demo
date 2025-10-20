package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.proto.grpc.GetCustomerResponse;
import com.worsun.spring_demo.home.mapper.QueryResultMapper;
import com.worsun.spring_demo.home.model.QueryResult;
import com.worsun.spring_demo.home.model.RemoteQueryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueryGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(QueryGatewayService.class);

    private final HttpQueryClient httpQueryClient;
    private final GrpcQueryClient grpcQueryClient;
    private final QueryResultMapper queryResultMapper;

    public QueryGatewayService(HttpQueryClient httpQueryClient,
            GrpcQueryClient grpcQueryClient,
            QueryResultMapper queryResultMapper) {
        this.httpQueryClient = httpQueryClient;
        this.grpcQueryClient = grpcQueryClient;
        this.queryResultMapper = queryResultMapper;
    }

    public QueryResult queryViaHttp(String id) {
        logger.info("[HOME-GATEWAY] Starting HTTP query for ID: {}", id);
        logger.info("[HOME-GATEWAY] Calling httpQueryClient.fetchRecord with ID: {}", id);
        RemoteQueryRecord record = httpQueryClient.fetchRecord(id);
        logger.info("[HOME-GATEWAY] Received record from httpQueryClient: {}", record);

        if (record == null) {
            logger.warn("[HOME-GATEWAY] No data returned from HTTP query service for ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data returned from HTTP query service");
        }

        logger.info("[HOME-GATEWAY] Mapping record to QueryResult");
        QueryResult result = queryResultMapper.mapToResult(record.getId(), record.getName(), record.getEmail());
        logger.info("[HOME-GATEWAY] Mapped result: {}", result);
        logger.info("[HOME-GATEWAY] Completed HTTP query for ID: {}", id);
        return result;
    }

    public QueryResult queryViaGrpc(String id) {
        logger.info("[HOME-GATEWAY] Starting gRPC query for ID: {}", id);
        logger.info("[HOME-GATEWAY] Calling grpcQueryClient.fetchRecord with ID: {}", id);
        GetCustomerResponse response = grpcQueryClient.fetchRecord(id);
        logger.info("[HOME-GATEWAY] Received response from grpcQueryClient: {}", response);

        logger.info("[HOME-GATEWAY] Mapping response to QueryResult");
        QueryResult result = queryResultMapper.mapToResult(
                String.valueOf(response.getCustomer().getId()),
                response.getCustomer().getName(),
                response.getCustomer().getEmail());
        logger.info("[HOME-GATEWAY] Mapped result: {}", result);
        logger.info("[HOME-GATEWAY] Completed gRPC query for ID: {}", id);
        return result;
    }
}