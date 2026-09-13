package io.github.noel0.modelregistry;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class ModelsEndpointTest {
    @Test
    void missingCredentialsReturn401() {
        given().when().get("/v1/models").then()
                .statusCode(401)
                .body("error.type", equalTo("authentication_error"));
    }

    @Test
    void invalidCredentialsReturn401() {
        given().header("Authorization", "Bearer not-known")
                .when().get("/v1/models").then()
                .statusCode(401);
    }

    @Test
    void validCredentialWithoutScopeReturns403() {
        given().header("Authorization", "Bearer no-models-token")
                .when().get("/v1/models").then()
                .statusCode(403)
                .body("error.type", equalTo("permission_error"));
    }

    @Test
    void exactScopeReturnsOpenAiListOfEnabledModels() {
        given().header("Authorization", "Bearer reader-token")
                .when().get("/v1/models").then()
                .statusCode(200)
                .body("object", equalTo("list"))
                .body("data", hasSize(2))
                .body("data.id", contains("llama-3.3-70b", "qwen-3.5-27b"))
                .body("data[0].object", equalTo("model"))
                .body("data[0].created", equalTo(1710000000))
                .body("data[0].owned_by", equalTo("system"));
    }

    @Test
    void supportedWildcardScopesReturn200() {
        for (String token : new String[]{
                "model-admin-token", "global-reader-token", "admin-token"}) {
            given().header("Authorization", "Bearer " + token)
                    .when().get("/v1/models").then()
                    .statusCode(200);
        }
    }
}
