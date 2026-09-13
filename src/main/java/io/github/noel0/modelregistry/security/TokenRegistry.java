package io.github.noel0.modelregistry.security;

import java.util.Optional;

public interface TokenRegistry {
    Optional<TokenPrincipal> find(String rawToken);
}
