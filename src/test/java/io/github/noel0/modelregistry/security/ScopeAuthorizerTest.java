package io.github.noel0.modelregistry.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScopeAuthorizerTest {
    private final ScopeAuthorizer authorizer = new ScopeAuthorizer();

    @ParameterizedTest
    @ValueSource(strings = {"models:read", "models:*", "*:read", "*:*"})
    void supportedScopesPermitModelReads(String scope) {
        assertTrue(authorizer.permits(Set.of(scope), "models", "read"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"tasks:read", "models:write", "*:write", "models"})
    void unrelatedScopesDoNotPermitModelReads(String scope) {
        assertFalse(authorizer.permits(Set.of(scope), "models", "read"));
    }
}
