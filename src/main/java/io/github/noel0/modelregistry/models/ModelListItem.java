package io.github.noel0.modelregistry.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelListItem(
        String id,
        String object,
        long created,
        @JsonProperty("owned_by") String ownedBy) {

    static ModelListItem from(ModelDescriptor model) {
        return new ModelListItem(model.id(), model.object(), model.created(), model.ownedBy());
    }
}
