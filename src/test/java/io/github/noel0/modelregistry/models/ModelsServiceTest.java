package io.github.noel0.modelregistry.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelsServiceTest {
    @Test
    void returnsOnlyEnabledModelsInRegistryOrder() {
        ModelRegistry registry = () -> List.of(
                new ModelDescriptor("first", 10, "system", true),
                new ModelDescriptor("hidden", 11, "system", false),
                new ModelDescriptor("second", 12, "team", true));

        ModelListResponse response = new ModelsService(registry).listEnabled();

        assertEquals("list", response.object());
        assertEquals(List.of("first", "second"),
                response.data().stream().map(ModelListItem::id).toList());
        assertEquals("model", response.data().getFirst().object());
        assertEquals(10, response.data().getFirst().created());
        assertEquals("system", response.data().getFirst().ownedBy());
    }
}
