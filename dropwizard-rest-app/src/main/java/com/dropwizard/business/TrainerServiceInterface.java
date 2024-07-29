package com.dropwizard.business;

import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerServiceInterface {

    // Save a new Trainer
    void saveTrainer(Trainer trainer);

    // Find all Trainers
    List<Trainer> findAllTrainers();

    // Find a Trainer by ID
    Optional<Trainer> findTrainerById(Long id);

    // Find all Pokémon for a Trainer by Trainer ID
    Set<Pokemon> findPokemonsByTrainerId(Long trainerId);

    // Find a specific Pokémon for a Trainer by Trainer ID and Pokémon ID
    Optional<Pokemon> findPokemonByTrainerAndPokemonId(Long trainerId, Long pokemonId);

    // Add a Pokémon to a Trainer's collection
    void addPokemonToTrainer(Long trainerId, Pokemon pokemon);

    // Release a Pokémon from a Trainer's collection
    void releasePokemonFromTrainer(Long trainerId, Long pokemonId);

    // Find all Trainers who have a specific Pokémon by its Pokédex ID
    List<Trainer> findTrainersByPokemonPokedexId(Long pokedexId);

    // Find the Trainer for a specific Pokémon by Pokémon ID
    Optional<Trainer> findTrainerByPokemonId(Long pokemonId);

    // Check if a Pokémon is owned by a Trainer
    boolean isPokemonOwnedByTrainer(Long trainerId, Long pokemonId);
}
