package org.acme.rest.client;

import io.smallrye.mutiny.Uni;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    @POST
    @Path("")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> test(final String body) {
        return Uni.createFrom().item(true);
    }
}
