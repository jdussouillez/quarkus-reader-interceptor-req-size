package org.acme.rest.client;

import io.smallrye.mutiny.Uni;
import java.io.File;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> test(final File file) {
        return Uni.createFrom().item(true);
    }
}
