# Spring Boot + LGTM Stack
Spring Boot application for managing employees and departments. It uses PostgreSQL for runtime persistence, H2 for tests, and Grafana LGTM for metrics, traces, and logs.

## Technology stack

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate ORM
- PostgreSQL 18
- H2 for tests
- OpenTelemetry
- Grafana LGTM
- Maven

## Configuration

Runtime database settings are read from environment variables.

Create a local `.env` file with values appropriate for your environment.


## Run locally with Docker Compose

Start PostgreSQL, Grafana LGTM, and the application:

```powershell
docker compose up --build
```

Services:

| Service | URL | Purpose |
| --- | --- | --- |
| HR application | http://localhost:8080 | REST API |
| Grafana LGTM | http://localhost:3000 | Dashboards, logs, metrics, and traces |
| PostgreSQL | localhost:5432 | Application database |

## Observability

The application exports telemetry to the `grafana-lgtm` Compose service through OTLP:

```text
Metrics: http://grafana-lgtm:4318/v1/metrics
Traces:  http://grafana-lgtm:4318/v1/traces
Logs:    http://grafana-lgtm:4318/v1/logs
```

The application also installs the OpenTelemetry Logback appender. Application logs from controllers and services can therefore be correlated with their distributed traces in Grafana.

Open Grafana at http://localhost:3000 and use the configured LGTM data sources to investigate:

1. Request latency in metrics.
2. The related distributed trace in Tempo.
3. Application errors and service logs in Loki.
