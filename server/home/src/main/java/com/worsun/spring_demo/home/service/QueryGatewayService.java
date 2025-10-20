package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.proto.grpc.GetCustomerResponse;
import com.worsun.spring_demo.home.mapper.QueryResultMapper;
import com.worsun.spring_demo.home.model.QueryResult;
import com.worsun.spring_demo.home.model.RemoteQueryRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueryGatewayService {

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
        RemoteQueryRecord record = httpQueryClient.fetchRecord(id);
        if (record == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data returned from HTTP query service");
        }
        return queryResultMapper.mapToResult(record.getId(), record.getName(), record.getEmail());
    }

    public QueryResult queryViaGrpc(String id) {
        GetCustomerResponse response = grpcQueryClient.fetchRecord(id);
        return queryResultMapper.mapToResult(
                String.valueOf(response.getCustomer().getId()),
                response.getCustomer().getName(),
                response.getCustomer().getEmail());
    }
}