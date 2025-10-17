package com.worsun.spring_demo.home.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "query.client")
public class QueryClientProperties {

    private final Http http = new Http();
    private final Grpc grpc = new Grpc();

    public Http getHttp() {
        return http;
    }

    public Grpc getGrpc() {
        return grpc;
    }

    public static class Http {
        /**
         * Base URL of the query service HTTP endpoint, for example
         * {@code http://localhost:8081}.
         */
        private String baseUrl = "http://localhost:8081";

        /**
         * Path template appended to the base URL, for example {@code /query/{id}}.
         */
        private String path = "/query/{id}";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Grpc {
        /**
         * Hostname of the query service gRPC endpoint.
         */
        private String host = "localhost";

        /**
         * Port of the query service gRPC endpoint.
         */
        private int port = 9090;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}