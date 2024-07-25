package com.dropwizard.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestRestController {

    @GET
    @Path("/hello")
    public String hi(){
        return "hello";
    }

}
