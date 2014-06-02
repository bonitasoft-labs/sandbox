package org.bonitasoft.poc.model.aggregation.util;

import java.util.List;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.model.aggregation.Papa;

public class FatherRepository {

    private final EntityManager entityManager;

    public FatherRepository(final EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public Papa save(final Papa papa) {
        return entityManager.merge(papa);
    }

    public Papa get(final Long id) {
        return entityManager.find(Papa.class, id);
    }

    public void remove(final Papa papa) {
        entityManager.remove(papa);
    }

    public List<Papa> getAll() {
        return entityManager.createQuery("FROM Papa").getResultList();
    }
}
