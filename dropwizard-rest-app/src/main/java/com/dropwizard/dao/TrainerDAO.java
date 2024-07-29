package com.dropwizard.dao;

import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class TrainerDAO {
    private final SessionFactory sessionFactory;

    @Inject
    public TrainerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save a new Trainer
    public void save(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(trainer);
            tx.commit();
        }
    }

    // Find a Trainer by ID
    public Optional<Trainer> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT t FROM Trainer t LEFT JOIN FETCH t.pokemons WHERE t.id = :id";
            Query<Trainer> query = session.createQuery(hql, Trainer.class);
            query.setParameter("id", id);

            Trainer trainer = query.uniqueResult();
            return Optional.ofNullable(trainer);
        }
    }


    // Find all Trainers with their associated Pokemons eagerly loaded
    public List<Trainer> findAll() {
        try (Session session = sessionFactory.openSession()) {
            // Simple query to get trainers without fetching pokemons
            String hql = "FROM Trainer";
            return session.createQuery(hql, Trainer.class).getResultList();
        }
    }


    // Update an existing Trainer
    public void update(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(trainer);
            tx.commit();
        }
    }

    // Delete a Trainer by ID
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Trainer trainer = session.get(Trainer.class, id);
            if (trainer != null) {
                session.delete(trainer);
            }
            tx.commit();
        }
    }

    // Add a Pokémon to a Trainer's collection
    public void addPokemonToTrainer(Long trainerId, Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Trainer trainer = session.get(Trainer.class, trainerId);
            if (trainer != null) {
                trainer.getPokemons().add(pokemon);
                pokemon.setTrainer(trainer); // Assuming a bi-directional relationship
                session.saveOrUpdate(pokemon);
                session.update(trainer);
            }
            tx.commit();
        }
    }

    // Release a Pokémon from a Trainer's collection
    public void releasePokemonFromTrainer(Long trainerId, Long pokemonId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Trainer trainer = session.get(Trainer.class, trainerId);
            Pokemon pokemon = session.get(Pokemon.class, pokemonId);
            if (trainer != null && pokemon != null) {
                trainer.getPokemons().remove(pokemon);
                pokemon.setTrainer(null); // Assuming a bi-directional relationship
                session.update(trainer);
                session.saveOrUpdate(pokemon);
            }
            tx.commit();
        }
    }

    // Find all Trainers who have a specific Pokémon by its Pokédex ID
    public List<Trainer> findTrainersByPokemonPokedexId(Long pokedexId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT t FROM Trainer t JOIN t.pokemons p WHERE p.id = :pokedexId";
            Query<Trainer> query = session.createQuery(hql, Trainer.class);
            query.setParameter("pokedexId", pokedexId);
            return query.list();
        }
    }

    // Find the trainer for a specific Pokémon by Pokémon ID
    public Optional<Trainer> findTrainerByPokemonId(Long pokemonId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT t FROM Trainer t JOIN t.pokemons p WHERE p.id = :pokemonId";
            Query<Trainer> query = session.createQuery(hql, Trainer.class);
            query.setParameter("pokemonId", pokemonId);

            Trainer trainer = query.uniqueResult();
            return Optional.ofNullable(trainer);
        }
    }



    public boolean isPokemonOwnedByTrainer(Long trainerId, Long pokemonId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(p) FROM Trainer t JOIN t.pokemons p WHERE t.id = :trainerId AND p.id = :pokemonId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("pokemonId", pokemonId);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
}
