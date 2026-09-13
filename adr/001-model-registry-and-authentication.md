# ADR 001: Model Registry and Authentication Boundary

Status: Accepted

Date: 2026-06-30

## Context

The prototype reproduces an OpenAI-compatible `GET /v1/models` endpoint while
exploring Quarkus security in isolation. It deliberately avoids a database, an
identity provider, containers, and any production integration.

Models are listed in configured order and filtered to those that are enabled.
Access uses opaque bearer tokens whose scopes match in four ways: exact
(`models:read`), resource wildcard (`models:*`), action wildcard (`*:read`), and
global wildcard (`*:*`).

## Decision

Use two in-memory, configuration-backed adapters:

1. `InMemoryModelRegistry` preserves configured order and exposes enabled state.
2. `InMemoryTokenRegistry` maps obvious demo bearer tokens to scope sets.

A JAX-RS authentication-priority request filter protects `GET /v1/models`. It
returns 401 when bearer authentication fails and 403 when authentication
succeeds but none of `models:read`, `models:*`, `*:read`, or `*:*` authorizes
the request. ADR 002 changes how the filter is attached to the endpoint.

Business logic remains independent of HTTP and configuration through
`ModelRegistry` and `TokenRegistry` interfaces.

## Consequences

- Unit tests exercise model filtering, bearer parsing, and scope matching
  without Quarkus startup.
- `@QuarkusTest` classes exercise the HTTP and security boundary.
- Demo tokens are plaintext configuration and are not production credentials.
- A real deployment would replace both in-memory registries with persistent
  adapters behind the same interfaces.

## Alternatives Rejected

- Database-backed registries: out of scope for a prototype, and they slow the
  feedback loop.
- OIDC/JWT: the target tokens are opaque, and those dependencies would test a
  different credential model.
- Token hashing in the prototype: belongs behind a persistent token adapter and
  adds cost without clarifying the HTTP contract.
