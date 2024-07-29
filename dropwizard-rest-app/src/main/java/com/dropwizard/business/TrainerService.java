package com.dropwizard.business;

import com.dropwizard.dao.TrainerDAO;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TrainerService implements TrainerServiceInterface{

    private final TrainerDAO trainerDao;


    @Inject
    public TrainerService(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
    }

    public void saveTrainer(Trainer trainer) {
        trainerDao.save(trainer);
    }

    public List<Trainer> findAllTrainers() {
        return trainerDao.findAll();
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerDao.findById(id);
    }

    public Set<Pokemon> findPokemonsByTrainerId(Long trainerId) {
        Optional<Trainer> trainer = trainerDao.findById(trainerId);
        // Eagerly fetch the Pokémon details within an open session
        return trainer.map(Trainer::getPokemons).orElseGet(Collections::emptySet);
    }

    public Optional<Pokemon> findPokemonByTrainerAndPokemonId(Long trainerId, Long pokemonId) {
        Optional<Trainer> trainer = trainerDao.findById(trainerId);
        return trainer.flatMap(t -> t.getPokemons().stream()
                .filter(p -> p.getId().equals(pokemonId))
                .findFirst());
    }

    public void addPokemonToTrainer(Long trainerId, Pokemon pokemon) {
        trainerDao.addPokemonToTrainer(trainerId, pokemon);
    }

    public void releasePokemonFromTrainer(Long trainerId, Long pokemonId) {
        trainerDao.releasePokemonFromTrainer(trainerId, pokemonId);
    }

    public List<Trainer> findTrainersByPokemonPokedexId(Long pokedexId) {
        return trainerDao.findTrainersByPokemonPokedexId(pokedexId);
    }

    public Optional<Trainer> findTrainerByPokemonId(Long pokemonId) {
        return trainerDao.findTrainerByPokemonId(pokemonId);
    }

    public boolean isPokemonOwnedByTrainer(Long trainerId, Long pokemonId) {
        return trainerDao.isPokemonOwnedByTrainer(trainerId, pokemonId);
    }
}
