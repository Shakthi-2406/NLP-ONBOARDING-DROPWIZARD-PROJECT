package com.dropwizard.dao;

import com.dropwizard.constant.PokemonType;
import com.dropwizard.constant.Gender;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PokemonDAO {
    private final SessionFactory sessionFactory;

    @Inject
    public PokemonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save a new Pokémon
    public void save(Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            if (pokemon.getId() == null) {
                session.save(pokemon);
            } else {
                session.update(pokemon);
            }
            tx.commit();
        }
    }


    public List<Pokemon> findPokemonsByTrainerId(Long trainerId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Trainer t JOIN t.pokemons p WHERE t.id = :trainerId";
            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("trainerId", trainerId);
            List<Pokemon> pokemons = query.list();
            // Initialize pokemonTypes and gender
            for (Pokemon pokemon : pokemons) {
                Hibernate.initialize(pokemon.getPokemonTypes());
                Hibernate.initialize(pokemon.getGender());
            }
            return pokemons;
        }
    }


    // Find a Pokémon by ID with eager loading of Pokémon types and gender
    public Optional<Pokemon> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Pokemon p LEFT JOIN FETCH p.pokemonTypes LEFT JOIN FETCH p.gender WHERE p.id = :id";
            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("id", id);

            Pokemon pokemon = query.uniqueResult();
            return Optional.ofNullable(pokemon);
        }
    }

    // Find a Pokémon by name, level, type, and gender
    public Optional<Pokemon> findPokemonByNameLevelTypeAndGender(
            String name, Integer level, Set<PokemonType> types, Gender gender) {
        try (Session session = sessionFactory.openSession()) {
            // Define HQL query
            String hql = "SELECT p FROM Pokemon p "
                    + "JOIN p.pokemonTypes pt "
                    + "WHERE p.name = :name "
                    + "AND p.level = :level "
                    + "AND pt IN :types "
                    + "AND p.gender = :gender";

            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("name", name);
            query.setParameter("level", level);
            query.setParameterList("types", types.toArray(new PokemonType[0])); // Convert Set to array
            query.setParameter("gender", gender);

            // Execute query and return result
            Pokemon pokemon = query.uniqueResult();
            return Optional.ofNullable(pokemon);
        }
    }


    // Find all Pokémon with eager loading of Pokémon types and gender
    public List<Pokemon> findAll() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Pokemon p LEFT JOIN FETCH p.pokemonTypes LEFT JOIN FETCH p.gender";
            return session.createQuery(hql, Pokemon.class).list();
        }
    }

    // Update an existing Pokémon
    public void update(Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(pokemon);
            tx.commit();
        }
    }

    // Delete a Pokémon by ID
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Pokemon pokemon = session.get(Pokemon.class, id);
            if (pokemon != null) {
                session.delete(pokemon);
            }
            tx.commit();
        }
    }

    // Find Pokémon by their type with eager loading of Pokémon types and gender
    public List<Pokemon> findByType(PokemonType type) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Pokemon p JOIN p.pokemonTypes t LEFT JOIN FETCH p.gender WHERE t = :type";
            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("type", type);
            return query.list();
        }
    }

    // Find all Pokémon by gender with eager loading of Pokémon types and gender
    public List<Pokemon> findByGender(Gender gender) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Pokemon p LEFT JOIN FETCH p.pokemonTypes LEFT JOIN FETCH p.gender WHERE p.gender = :gender";
            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("gender", gender);
            return query.list();
        }
    }

    // Find all trainers who have a specific Pokémon
    public List<Trainer> findTrainersByPokemonId(Long pokemonId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT t FROM Trainer t JOIN t.pokemons p WHERE p.id = :pokemonId";
            Query<Trainer> query = session.createQuery(hql, Trainer.class);
            query.setParameter("pokemonId", pokemonId);
            return query.list();
        }
    }
}
