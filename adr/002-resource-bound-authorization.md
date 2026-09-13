# ADR 002: Bind Authorization to the Resource, Not the Path

Status: Accepted

Date: 2026-09-13

Supersedes the path-matching clause of ADR 001 ("A JAX-RS authentication-priority
request filter protects only `GET /v1/models`").

## Context

ADR 001 implemented the boundary as a global request filter that decided whether to
act by comparing the request path to `v1/models`. A leading-slash mismatch in that
comparison was found and fixed during the original work.

Preparing the prototype for review, a probe of path spellings showed a second variant:
`GET /v1/models/` with no credentials returned 200 and the model list. The string
comparison failed, the filter returned early, and JAX-RS still dispatched the request to
`ModelsResource`. `//v1/models`, `/v1//models` and `/v1/./models` were already rejected.

Normalizing one more spelling would repeat the same design: the filter would still
protect a string rather than the endpoint.

## Decision

Bind `ModelsSecurityFilter` to `ModelsResource` with a JAX-RS `@NameBinding`
annotation, `@RequiresModelsRead`, and remove the path and method comparison from the
filter. Authentication and authorization now run for every request JAX-RS dispatches to
the annotated resource, however the path was spelled.

The 401/403 contract from ADR 001 is unchanged.

## Consequences

- Protection follows routing, so new path spellings cannot silently bypass the filter.
- Each protected resource must carry the annotation. A resource without it is
  unprotected, which is visible in code review rather than hidden in a path table.
- `PathVariantsTest` asserts 401 for every routed spelling, and 200 for an authorized
  caller on the trailing-slash form.

## Alternatives Rejected

- Normalizing trailing slashes in the filter: fixes this spelling, keeps the design flaw.
- Rejecting non-canonical paths globally: changes routing behavior for every endpoint to
  patch one filter.
