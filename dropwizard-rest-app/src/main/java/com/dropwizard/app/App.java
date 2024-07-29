package com.dropwizard.app;

import com.dropwizard.resource.PokemonTrainerResource;
import com.dropwizard.resource.TestRestController;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Configuration;

public class App extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);


    @Override
    public void run(Configuration c, Environment e) throws Exception {
        LOGGER.info("Registering REST resources");
        e.jersey().register(new TestRestController());
        e.jersey().register(new PokemonTrainerResource());
        e.jersey().register(new JsonProcessingExceptionMapper(true));
        System.out.println("HERE");
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}