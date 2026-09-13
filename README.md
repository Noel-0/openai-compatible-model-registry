# OpenAI-compatible model registry

Authenticated `GET /v1/models` service in Java and Quarkus: bearer tokens,
scope-based authorization, and tests that check every boundary. Built as a
standalone prototype during my software engineering internship at XpressAI.

[![test](https://github.com/Noel-0/openai-compatible-model-registry/actions/workflows/test.yml/badge.svg)](https://github.com/Noel-0/openai-compatible-model-registry/actions/workflows/test.yml)

## Authorization

| Request                   | Token scope   | Result                     |
|---------------------------|---------------|----------------------------|
| no `Authorization` header | —             | 401 `authentication_error` |
| unknown token             | —             | 401 `authentication_error` |
| `no-models-token`         | `tasks:read`  | 403 `permission_error`     |
| `reader-token`            | `models:read` | 200, enabled models only   |
| `model-admin-token`       | `models:*`    | 200                        |
| `global-reader-token`     | `*:read`      | 200                        |
| `admin-token`             | `*:*`         | 200                        |

The same rules hold for every path spelling that routes to the endpoint
(`/v1/models/`, `//v1/models`, `/v1//models`, `/v1/./models`).
Tokens are demo fixtures in `src/main/resources/application.properties`, not credentials.

## Design

HTTP resource → security filter → model service → registry. Each layer sits
behind an interface, so each boundary is tested on its own.

The filter is bound to the resource with a JAX-RS `@NameBinding` annotation
instead of matching the request path, so authorization follows routing.

Decisions: [ADR 001](adr/001-model-registry-and-authentication.md) ·
[ADR 002](adr/002-resource-bound-authorization.md)

## Security note

A probe of path spellings found that `GET /v1/models/`, with a trailing slash and no
credentials, returned 200. The original filter matched the request path as a string, so a
spelling it didn't recognize skipped authentication while JAX-RS still routed the request.
The fix binds the filter to the resource instead, and `PathVariantsTest` checks every
routed spelling, failing against the old filter. Details: [ADR 002](adr/002-resource-bound-authorization.md).

## Run

Requires JDK 21 and Maven.

    mvn test          # 22 tests
    mvn quarkus:dev
    curl -H 'Authorization: Bearer reader-token' localhost:8080/v1/models
## Quick start
Start the service, then request the enabled model list with a reader token:
```bash
curl -H 'Authorization: Bearer reader-token' http://localhost:8080/v1/models
```
The example uses the demo token from `src/main/resources/application.properties`; do not reuse it in production.
