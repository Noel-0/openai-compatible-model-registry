package io.github.noel0.modelregistry.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@RequiresModelsRead
@Priority(Priorities.AUTHENTICATION)
public class ModelsSecurityFilter implements ContainerRequestFilter {
    @Inject
    BearerAuthenticator authenticator;

    @Inject
    ScopeAuthorizer authorizer;

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        var principal = authenticator.authenticate(
                request.getHeaderString(HttpHeaders.AUTHORIZATION));
        if (principal.isEmpty()) {
            request.abortWith(error(
                    Response.Status.UNAUTHORIZED,
                    "Missing or invalid bearer token",
                    "authentication_error"));
            return;
        }

        if (!authorizer.permits(principal.get().scopes(), "models", "read")) {
            request.abortWith(error(
                    Response.Status.FORBIDDEN,
                    "The token lacks the models:read scope",
                    "permission_error"));
        }
    }

    private Response error(Response.Status status, String message, String type) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ApiError(message, type))
                .build();
    }
}
