package org.bonitasoft.poc.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.bonitasoft.poc.util.PersistenceUtil;
import org.junit.After;

public abstract class AbstractTest {

    protected static PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();

    @After
    public void tearDown() {
        for (final Class<?> entityType : getEntityTypes()) {
            deleteEntity(entityType);
        }
    }

    public abstract List<Class<?>> getEntityTypes();

    protected <T> void deleteEntity(final Class<T> entityType) {
        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = cb.createQuery(entityType);
        final Root<T> allInstances = criteria.from(entityType);
        final CriteriaQuery<T> all = criteria.select(allInstances);
        for (final Object car : entityManager.createQuery(all).getResultList()) {
            entityManager.remove(car);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
