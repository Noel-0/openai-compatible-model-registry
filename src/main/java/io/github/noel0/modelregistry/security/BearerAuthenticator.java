package io.github.noel0.modelregistry.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class BearerAuthenticator {
    private static final String PREFIX = "Bearer ";
    private final TokenRegistry tokenRegistry;

    public BearerAuthenticator(TokenRegistry tokenRegistry) {
        this.tokenRegistry = tokenRegistry;
    }

    public Optional<TokenPrincipal> authenticate(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIX)) {
            return Optional.empty();
        }
        String rawToken = authorizationHeader.substring(PREFIX.length()).trim();
        if (rawToken.isEmpty()) {
            return Optional.empty();
        }
        return tokenRegistry.find(rawToken);
    }
}
