package com.dropwizard.resource;

import com.dropwizard.client.PokemonTrainerFunctionalityMain;
import com.dropwizard.dto.PokemonDTO;
import com.dropwizard.dto.TrainerDTO;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("/pokemon-trainer")
@Produces(MediaType.APPLICATION_JSON)
public class PokemonTrainerResource {

    private final PokemonTrainerFunctionalityMain functionalityMain;

    @Inject
    public PokemonTrainerResource() {
        this.functionalityMain = new PokemonTrainerFunctionalityMain();
    }

    // 1. Get all trainers
    @GET
    @Path("/trainers")
    public Response getAllTrainers() {
        List<TrainerDTO> trainers = functionalityMain.getAllTrainers();
        return trainers.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).entity("No trainers found").build()
                : Response.ok(trainers).build();
    }

    // 2. Add a new trainer
    @POST
    @Path("/trainers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewTrainer(Trainer trainer) {
        trainer.setDateAdded(new Date());
        TrainerDTO newTrainerDTO = functionalityMain.addNewTrainer(trainer.getName(), trainer.getAge());
        return Response.status(Response.Status.CREATED).entity(newTrainerDTO).build();
    }

    // 3. Get a trainer by ID
    @GET
    @Path("/trainers/{id}")
    public Response getTrainerById(@PathParam("id") Long id) {
        Optional<TrainerDTO> trainer = functionalityMain.getTrainerById(id);
        return trainer.map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Trainer not found with ID: " + id)
                        .build());
    }


    // 4. Get all Pokémon for a trainer by trainer ID
    @GET
    @Path("/trainers/{trainerId}/pokemons")
    public Response getPokemonsByTrainerId(@PathParam("trainerId") Long trainerId) {
        List<PokemonDTO> pokemons = functionalityMain.getPokemonsByTrainerId(trainerId); // Method should return List<Pokemon>
        return pokemons.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).entity("No Pokémon found for Trainer ID: " + trainerId).build()
                : Response.ok(pokemons).build();
    }

    // 5. Get a specific Pokémon for a trainer by trainer ID and Pokémon ID
    @GET
    @Path("/trainers/{trainerId}/pokemons/{pokemonId}")
    public Response getPokemonByTrainerAndId(@PathParam("trainerId") Long trainerId, @PathParam("pokemonId") Long pokemonId) {
        Optional<PokemonDTO> pokemon = functionalityMain.getPokemonByTrainerAndId(trainerId, pokemonId); // Method should return Optional<Pokemon>
        return pokemon.map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                    .entity("Pokémon not found for Trainer ID: " + trainerId + " and Pokémon ID: " + pokemonId)
                    .build());
    }


    // 6. Update Pokémon details
    @PUT
    @Path("/trainers/{trainerId}/pokemons/{pokemonId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePokemonDetails(@PathParam("trainerId") Long trainerId,
                                         @PathParam("pokemonId") Long pokemonId,
                                         Pokemon updatedPokemon) {
        functionalityMain.updatePokemonDetails(trainerId, pokemonId, updatedPokemon.getLevel(), updatedPokemon.getNickname());
        return Response.ok("Updated Pokémon details").build();
    }


    // 7. Add a Pokémon to a trainer
    @POST
    @Path("/trainers/{trainerId}/pokemons")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPokemonToTrainer(@PathParam("trainerId") Long trainerId, PokemonDTO pokemon) {
        functionalityMain.addPokemonToTrainer(trainerId, pokemon);
        return Response.status(Response.Status.CREATED).entity("Added Pokémon to Trainer ID: " + trainerId).build();
    }

    // 8. Release a Pokémon from a trainer
    @DELETE
    @Path("/trainers/{trainerId}/pokemons/{pokemonId}")
    public Response releasePokemonFromTrainer(@PathParam("trainerId") Long trainerId, @PathParam("pokemonId") Long pokemonId) {
        functionalityMain.releasePokemonFromTrainer(trainerId, pokemonId);
        return Response.ok("Released Pokémon ID: " + pokemonId + " from Trainer ID: " + trainerId).build();
    }

    // 9. Get trainers by Pokémon Pokédex ID
    @GET
    @Path("/pokemons/{pokedexId}/trainers")
    public Response getTrainersByPokemonPokedexId(@PathParam("pokedexId") Long pokedexId) {
        List<TrainerDTO> trainers = functionalityMain.getTrainersByPokemonPokedexId(pokedexId); // Method should return List<Trainer>
        return trainers.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).entity("No trainers found with Pokémon Pokédex ID: " + pokedexId).build()
                : Response.ok(trainers).build();
    }

    // 10. Get trainer by Pokémon ID
    @GET
    @Path("/pokemons/{pokemonId}/trainer")
    public Response getTrainerByPokemonId(@PathParam("pokemonId") Long pokemonId) {
        Optional<TrainerDTO> trainer = functionalityMain.getTrainerByPokemonId(pokemonId); // Method should return Optional<Trainer>
        return trainer.map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                    .entity("Trainer not found for Pokémon ID: " + pokemonId)
                    .build());
    }
}
