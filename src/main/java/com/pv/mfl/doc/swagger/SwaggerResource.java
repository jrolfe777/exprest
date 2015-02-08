package com.pv.mfl.doc.swagger;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/doc")
@Produces(MediaType.TEXT_HTML)
public class SwaggerResource {
    @GET
    public View get() {
        return new SwaggerView();
    }
}
