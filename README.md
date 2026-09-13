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

## The bug worth reading about

> **Before publishing:** replace this with four sentences in your own words: the
> leading-slash bypass you fixed first, the trailing-slash variant you found while
> preparing this release, why a string patch wasn't enough, and the test that now
> guards it.

## Run

Requires JDK 21 and Maven.

    mvn test          # 22 tests
    mvn quarkus:dev
    curl -H 'Authorization: Bearer reader-token' localhost:8080/v1/models
