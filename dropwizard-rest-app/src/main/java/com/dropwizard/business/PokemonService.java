package com.dropwizard.business;

import com.dropwizard.dao.PokemonDAO;
import com.dropwizard.model.Pokemon;

import java.util.List;

public class PokemonService {

    private final PokemonDAO pokemonDao;

    public PokemonService(PokemonDAO pokemonDao) {
        this.pokemonDao = pokemonDao;
    }

    public void save(Pokemon pokemon) {
        pokemonDao.save(pokemon);
    }

    public List<Pokemon> findAll() {
        return pokemonDao.findAll();
    }
}
