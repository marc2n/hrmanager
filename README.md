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
- JaCoCo coverage reporting

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

## Tests and coverage

The project uses service and controller unit tests plus Spring Boot / MockMvc integration tests with H2. JaCoCo 0.8.14 collects coverage during the tests and generates a report in the Maven `test` phase.

With JDK 21 installed, run:

```bash
chmod +x mvnw
./mvnw -B -ntp clean verify
```

On Windows PowerShell:

```powershell
.\mvnw.cmd -B -ntp clean verify
```

After a successful run, open `target/site/jacoco/index.html` in a browser to inspect coverage by package and class. Surefire test results are available in `target/surefire-reports/`.

The [CI workflow](.github/workflows/ci.yml) runs the same Maven command and uploads two artifacts:

| Artifact | Contents |
| --- | --- |
| `test-reports` | Surefire test results |
| `jacoco-coverage-report` | JaCoCo coverage report |

To view coverage from CI, open a completed run under [GitHub Actions](https://github.com/marc2n/hrmanager/actions/workflows/ci.yml), download `jacoco-coverage-report` from its artifacts, extract the archive, and open `index.html`.

[The run for the JaCoCo addition](https://github.com/marc2n/hrmanager/actions/runs/36871994430) passed all 32 tests with no failures, errors, or skipped tests, generated the coverage report, and uploaded it successfully.

Coverage is currently reported, not enforced: the POM does not configure a `jacoco:check` goal or a minimum coverage threshold. H2 integration tests also do not replace testing against PostgreSQL.

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
