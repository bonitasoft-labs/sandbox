package org.bonitasoft.poc.model.aggregation.util;

import java.util.List;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.model.aggregation.Enfant;

public class ChildRepository {

    private final EntityManager entityManager;

    public ChildRepository(final EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public Enfant save(final Enfant enfant) {
        return entityManager.merge(enfant);
    }

    public Enfant get(final Long id) {
        return entityManager.find(Enfant.class, id);
    }

    public List<Enfant> getAll() {
        return entityManager.createQuery("FROM Enfant").getResultList();
    }

    public void remove(final Enfant enfant) {
        entityManager.remove(enfant);
    }
}
