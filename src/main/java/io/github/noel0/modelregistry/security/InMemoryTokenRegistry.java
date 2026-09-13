package io.github.noel0.modelregistry.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryTokenRegistry implements TokenRegistry {
    private final Map<String, TokenPrincipal> tokens;

    public InMemoryTokenRegistry(
            @ConfigProperty(name = "auth.tokens.entries") String configuredTokens) {
        this.tokens = parse(configuredTokens);
    }

    static Map<String, TokenPrincipal> parse(String configuredTokens) {
        if (configuredTokens == null || configuredTokens.isBlank()) {
            return Map.of();
        }
        return Arrays.stream(configuredTokens.split(";"))
                .map(entry -> entry.split("=", 2))
                .collect(Collectors.toUnmodifiableMap(
                        fields -> fields[0],
                        fields -> new TokenPrincipal(fields[0], Set.of(fields[1].split(",")))));
    }

    @Override
    public Optional<TokenPrincipal> find(String rawToken) {
        return Optional.ofNullable(tokens.get(rawToken));
    }
}
