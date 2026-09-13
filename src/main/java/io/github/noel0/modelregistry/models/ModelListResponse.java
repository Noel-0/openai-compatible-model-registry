package io.github.noel0.modelregistry.models;

import java.util.List;

public record ModelListResponse(String object, List<ModelListItem> data) {
    public ModelListResponse(List<ModelListItem> data) {
        this("list", List.copyOf(data));
    }
}
