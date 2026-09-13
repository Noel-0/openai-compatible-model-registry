package io.github.noel0.modelregistry.models;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelsService {
    private final ModelRegistry registry;

    public ModelsService(ModelRegistry registry) {
        this.registry = registry;
    }

    public ModelListResponse listEnabled() {
        return new ModelListResponse(registry.list().stream()
                .filter(ModelDescriptor::enabled)
                .map(ModelListItem::from)
                .toList());
    }
}
