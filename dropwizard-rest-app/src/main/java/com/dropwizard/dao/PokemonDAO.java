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

    public Optional<Pokemon> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Pokemon.class, id));
        }
    }

    public List<Pokemon> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Pokemon", Pokemon.class).list();
        }
    }

    public void update(Pokemon pokemon) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(pokemon);
            tx.commit();
        }
    }

    public void delete(int id) {
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
            Query<Pokemon> query = session.createQuery("SELECT p FROM Pokemon p JOIN p.types t WHERE t = :type", Pokemon.class);
            query.setParameter("type", type);
            return query.list();
        }
    }

}
