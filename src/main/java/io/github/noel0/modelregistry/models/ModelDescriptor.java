package io.github.noel0.modelregistry.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelDescriptor(
        String id,
        String object,
        long created,
        @JsonProperty("owned_by") String ownedBy,
        boolean enabled) {

    public ModelDescriptor(String id, long created, String ownedBy, boolean enabled) {
        this(id, "model", created, ownedBy, enabled);
    }
}
