package com.dropwizard.business;

import com.dropwizard.constant.PokemonType;
import com.dropwizard.constant.Gender;
import com.dropwizard.dao.PokemonDAO;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PokemonService {

    private final PokemonDAO pokemonDao;

    @Inject
    public PokemonService(PokemonDAO pokemonDao) {
        this.pokemonDao = pokemonDao;
    }

    // Save a new Pokémon
    public void savePokemon(Pokemon pokemon) {
        pokemonDao.save(pokemon);
    }

    // Find a Pokémon by ID
    public Optional<Pokemon> findPokemonById(Long id) {
        return pokemonDao.findById(id);
    }

    // Find all Pokémon
    public List<Pokemon> findAllPokemons() {
        return pokemonDao.findAll();
    }

    // Update an existing Pokémon
    public void updatePokemon(Pokemon pokemon) {
        pokemonDao.update(pokemon);
    }

    // Delete a Pokémon by ID
    public void deletePokemon(Long id) {
        pokemonDao.delete(id);
    }

    // Find Pokémon by their type
    public List<Pokemon> findPokemonsByType(PokemonType type) {
        return pokemonDao.findByType(type);
    }

    // Find Pokémon by their gender
    public List<Pokemon> findPokemonsByGender(Gender gender) {
        return pokemonDao.findByGender(gender);
    }

    // Find all trainers who have a specific Pokémon by Pokémon ID
    public List<Trainer> findTrainersByPokemonId(Long pokemonId) {
        return pokemonDao.findTrainersByPokemonId(pokemonId);
    }

    public List<Pokemon> findPokemonsByTrainerId(Long trainerId) {
        return pokemonDao.findPokemonsByTrainerId(trainerId);
    }

    public Optional<Pokemon> findPokemonByNameLevelTypeAndGender(String name, Integer level, Set<PokemonType> types, Gender gender) {
        return pokemonDao.findPokemonByNameLevelTypeAndGender(name, level, types, gender);
    }
}
