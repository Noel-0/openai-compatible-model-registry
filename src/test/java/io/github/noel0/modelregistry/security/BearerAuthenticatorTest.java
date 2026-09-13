package io.github.noel0.modelregistry.security;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BearerAuthenticatorTest {
    private final TokenPrincipal known = new TokenPrincipal("known", Set.of("models:read"));
    private final BearerAuthenticator authenticator =
            new BearerAuthenticator(token -> "known".equals(token) ? Optional.of(known) : Optional.empty());

    @Test
    void acceptsKnownBearerToken() {
        assertEquals(known, authenticator.authenticate("Bearer known").orElseThrow());
    }

    @Test
    void rejectsMissingMalformedBlankAndUnknownTokens() {
        assertTrue(authenticator.authenticate(null).isEmpty());
        assertTrue(authenticator.authenticate("Basic known").isEmpty());
        assertTrue(authenticator.authenticate("Bearer   ").isEmpty());
        assertTrue(authenticator.authenticate("Bearer unknown").isEmpty());
    }
}
