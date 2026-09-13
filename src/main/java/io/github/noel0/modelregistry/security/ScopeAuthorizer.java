package io.github.noel0.modelregistry.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

@ApplicationScoped
public class ScopeAuthorizer {
    public boolean permits(Set<String> scopes, String resource, String action) {
        return scopes.contains(resource + ":" + action)
                || scopes.contains(resource + ":*")
                || scopes.contains("*:" + action)
                || scopes.contains("*:*");
    }
}
