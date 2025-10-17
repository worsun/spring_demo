package com.worsun.spring_demo.home.service;

import com.worsun.spring_demo.home.config.QueryClientProperties;
import com.worsun.spring_demo.home.model.RemoteQueryRecord;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;

@Component
public class HttpQueryClient {

    private static final Logger log = LoggerFactory.getLogger(HttpQueryClient.class);

    private final RestTemplate restTemplate;
    private final QueryClientProperties clientProperties;

    public HttpQueryClient(RestTemplate restTemplate, QueryClientProperties clientProperties) {
        this.restTemplate = restTemplate;
        this.clientProperties = clientProperties;
    }

    public RemoteQueryRecord fetchRecord(String id) {
        QueryClientProperties.Http http = clientProperties.getHttp();
        URI uri = UriComponentsBuilder.fromHttpUrl(http.getBaseUrl())
                .path(http.getPath())
                .buildAndExpand(id)
                .toUri();
        try {
            ResponseEntity<RemoteQueryRecord> response = restTemplate.getForEntity(uri, RemoteQueryRecord.class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            log.error("HTTP query request failed for id {} with status {}", id, ex.getRawStatusCode(), ex);
            HttpStatus status = HttpStatus.resolve(ex.getRawStatusCode());
            if (status == null) {
                status = HttpStatus.BAD_GATEWAY;
            }
            throw new ResponseStatusException(status,
                    String.format("HTTP query service returned status %d", ex.getRawStatusCode()),
                    ex);
        } catch (ResourceAccessException ex) {
            log.error("Unable to reach HTTP query service for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to reach HTTP query service", ex);
        } catch (RestClientException ex) {
            log.error("HTTP query request failed for id {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error when calling HTTP query service", ex);
        }
    }
}