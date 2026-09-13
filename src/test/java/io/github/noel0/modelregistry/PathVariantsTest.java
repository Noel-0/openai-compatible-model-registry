package io.github.noel0.modelregistry;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Regression guard: every path spelling that JAX-RS routes to the models endpoint must be
 * authenticated. A trailing slash previously skipped the path-matched filter and returned 200.
 */
@QuarkusTest
class PathVariantsTest {
    @ParameterizedTest
    @ValueSource(strings = {"/v1/models", "/v1/models/", "//v1/models", "/v1//models", "/v1/./models"})
    void everyRoutedSpellingRequiresABearerToken(String path) {
        given().urlEncodingEnabled(false)
                .when().get(path).then()
                .statusCode(401)
                .body("error.type", equalTo("authentication_error"));
    }

    @Test
    void trailingSlashStillServesAuthorizedCallers() {
        given().urlEncodingEnabled(false)
                .header("Authorization", "Bearer reader-token")
                .when().get("/v1/models/").then()
                .statusCode(200)
                .body("object", equalTo("list"));
    }
}
