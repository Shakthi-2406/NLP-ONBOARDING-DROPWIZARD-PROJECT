package com.dropwizard.dao;

import com.dropwizard.constant.PokemonType;
import com.dropwizard.model.Pokemon;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class PokemonDAO {
    private final SessionFactory sessionFactory;

    @Inject
    public PokemonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(pokemon);
            tx.commit();
        }
    }

    public Optional<Pokemon> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            // Use fetch join to eagerly load the pokemonTypes collection
            String hql = "SELECT p FROM Pokemon p LEFT JOIN FETCH p.pokemonTypes WHERE p.id = :id";
            Query<Pokemon> query = session.createQuery(hql, Pokemon.class);
            query.setParameter("id", id);

            // Retrieve the result list; since id is unique, the list will have at most one result
            Pokemon pokemon = query.uniqueResult();

            return Optional.ofNullable(pokemon);
        }
    }


    public List<Pokemon> findAll() {
        try (Session session = sessionFactory.openSession()) {
            // Fetch Pokemon with PokemonTypes eagerly
            return session.createQuery("SELECT p FROM Pokemon p LEFT JOIN FETCH p.pokemonTypes", Pokemon.class)
                    .list();
        }
    }

    public void update(Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(pokemon);
            tx.commit();
        }
    }

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

    public List<Pokemon> findByType(PokemonType type) {
        try (Session session = sessionFactory.openSession()) {
            // Using the proper join and parameter binding
            Query<Pokemon> query = session.createQuery("SELECT p FROM Pokemon p JOIN p.pokemonTypes t WHERE t = :type", Pokemon.class);
            query.setParameter("type", type);
            return query.list();
        }
    }
}
