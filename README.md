# E-Commerce Observability Lab (v2) — Metrics + Logs + Traces + Resilience

This repo is a **production-support practice playground**:
- **2 Spring Boot services** (Spring MVC) with **CRUD** + service-to-service calls via **WebClient (blocking)**.
- **Postgres** for persistence (Flyway migrations).
- **Actuator + Micrometer Prometheus** metrics.
- **Structured JSON logs** shipped to **Elasticsearch** via **Filebeat**, visualized in **Kibana**.
- **Distributed tracing** via **OpenTelemetry (OTLP)** exported to **Tempo**, visualized in **Grafana**.
- **Resilience4j** (retry + circuit breaker) around service-to-service calls.

## Quick start

```bash
docker compose up -d --build
docker compose ps
```

### URLs
- product swagger: http://localhost:8081/swagger-ui.html
- order swagger:   http://localhost:8082/swagger-ui.html
- Prometheus:      http://localhost:9090
- Grafana:         http://localhost:3000  (admin/admin)
- Kibana:          http://localhost:5601

## Traces (Tempo + Grafana)

Grafana has Tempo datasource pre-provisioned.
- Grafana → Explore → Tempo → search by `service.name` = `product-service` / `order-service`

## Resilience4j test

Stop product-service:
```bash
docker compose stop product-service
```

Try creating an order (should retry and then open the circuit breaker), then start it back:
```bash
docker compose start product-service
```
