package io.github.noel0.modelregistry.security;

import java.util.Set;

public record TokenPrincipal(String token, Set<String> scopes) {
    public TokenPrincipal {
        scopes = Set.copyOf(scopes);
    }
}
