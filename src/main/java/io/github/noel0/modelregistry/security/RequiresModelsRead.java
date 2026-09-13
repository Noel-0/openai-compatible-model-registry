package io.github.noel0.modelregistry.security;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds {@link ModelsSecurityFilter} to the annotated resource classes or methods, so
 * authorization follows the endpoint that JAX-RS actually dispatches to rather than a
 * path string that request spellings can drift away from.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiresModelsRead {
}
