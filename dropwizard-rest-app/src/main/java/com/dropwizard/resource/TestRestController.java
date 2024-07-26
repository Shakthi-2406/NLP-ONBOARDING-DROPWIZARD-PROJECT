package com.dropwizard.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.dropwizard.client.TestApplicationClient.test;


@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestRestController {

    @GET
    @Path("/hello")
    public String hi(){
        return "hello";
    }

    @GET
    @Path("/register")
    public String registerCustomer() {
        test();
        return "register";
    }

}
