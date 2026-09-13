package io.github.noel0.modelregistry.models;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class InMemoryModelRegistry implements ModelRegistry {
    private final List<ModelDescriptor> models;

    public InMemoryModelRegistry(
            @ConfigProperty(name = "models.registry.entries") String configuredModels) {
        this.models = parse(configuredModels);
    }

    static List<ModelDescriptor> parse(String configuredModels) {
        if (configuredModels == null || configuredModels.isBlank()) {
            return List.of();
        }
        return Arrays.stream(configuredModels.split(";"))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .map(InMemoryModelRegistry::parseEntry)
                .toList();
    }

    private static ModelDescriptor parseEntry(String entry) {
        String[] fields = entry.split("\\|", -1);
        if (fields.length != 4) {
            throw new IllegalArgumentException("Model entry must have id|created|enabled|owned_by: " + entry);
        }
        return new ModelDescriptor(
                fields[0],
                Long.parseLong(fields[1]),
                fields[3],
                Boolean.parseBoolean(fields[2]));
    }

    @Override
    public List<ModelDescriptor> list() {
        return models;
    }
}
