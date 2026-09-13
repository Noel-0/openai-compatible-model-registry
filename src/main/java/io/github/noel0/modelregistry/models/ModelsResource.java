package io.github.noel0.modelregistry.models;

import io.github.noel0.modelregistry.security.RequiresModelsRead;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/models")
@RequiresModelsRead
@Produces(MediaType.APPLICATION_JSON)
public class ModelsResource {
    private final ModelsService modelsService;

    public ModelsResource(ModelsService modelsService) {
        this.modelsService = modelsService;
    }

    @GET
    public ModelListResponse list() {
        return modelsService.listEnabled();
    }
}
