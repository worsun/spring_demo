# home

Home 服务是一个 Spring Boot 2.7 应用，演示如何通过 HTTP 和 gRPC 调用上游的 query 服务，并将结果暴露为两个 REST 接口。

## 功能

- `GET /api/query/http/{id}`：通过 HTTP 调用 query 服务获取指定 `id` 的记录。
- `GET /api/query/grpc/{id}`：通过 gRPC 调用 query 服务获取指定 `id` 的记录。

两个接口都返回统一的 `QueryResult` JSON 响应。

## 配置

在 `application.yml` 中配置 query 服务的地址：

```yaml
query:
  client:
    http:
      base-url: http://localhost:8081
      path: /query/{id}
    grpc:
      host: localhost
      port: 9090
```

## 构建

```bash
mvn compile
```

> **注意**：如需本地验证 gRPC 通信，请先启动 query 服务对应的 gRPC 端口。
