package io.github.noel0.modelregistry.security;

public record ApiError(ErrorBody error) {
    public ApiError(String message, String type) {
        this(new ErrorBody(message, type));
    }

    public record ErrorBody(String message, String type) {
    }
}
