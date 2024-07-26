package com.dropwizard.client;

import com.dropwizard.business.PokemonService;
import com.dropwizard.dao.PokemonDAO;
import com.dropwizard.model.Pokemon;
import org.hibernate.SessionFactory;

import com.dropwizard.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class TestApplicationClient {

    private static PokemonService pokemonService;

    public static void main(String[] args) {
        System.out.println("Welcome to Pokemon World!");
        initialize();
        test();
    }

    private static void initialize() {
        // Initialize SessionFactory
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Initialize PokemonDao
        PokemonDAO pokemonDao = new PokemonDAO(sessionFactory);

        // Initialize PokemonService
        pokemonService = new PokemonService(pokemonDao);
    }

    public static void test() {
        System.out.print("Enter name for Pokemon: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Pokemon pokemon = new Pokemon(name);
        pokemonService.save(pokemon);
        List<Pokemon> allPokemons = pokemonService.findAll();
        for (Pokemon pokemon1 : allPokemons) {
            System.out.println(pokemon1);
        }
    }
}
