package com.worsun.spring_demo.home.config;

import com.worsun.spring_demo.proto.grpc.CustomerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(QueryClientProperties.class)
public class ClientConfiguration {

    private final QueryClientProperties properties;

    public ClientConfiguration(QueryClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel queryManagedChannel() {
        QueryClientProperties.Grpc grpc = properties.getGrpc();
        return ManagedChannelBuilder.forAddress(grpc.getHost(), grpc.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public CustomerServiceGrpc.CustomerServiceBlockingStub queryServiceBlockingStub(
            ManagedChannel queryManagedChannel) {
        return CustomerServiceGrpc.newBlockingStub(queryManagedChannel);
    }
}