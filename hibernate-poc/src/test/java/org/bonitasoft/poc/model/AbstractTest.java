package org.bonitasoft.poc.model;

import java.util.List;
import java.util.Random;

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
        System.err.println("=== Cleaning all objects of types " + getEntityTypesToCleanAfterTest());
        for (final Class<?> entityType : getEntityTypesToCleanAfterTest()) {
            deleteEntity(entityType);
        }
        System.err.println("=== Cleaning done.");
    }

    public abstract List<Class<?>> getEntityTypesToCleanAfterTest();

    protected <T> void deleteEntity(final Class<T> entityType) {
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        for (final T entity : getAllEntities(entityType, entityManager)) {
            entityManager.remove(entity);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    protected <T> List<T> getAllEntities(final Class<T> entityType, final EntityManager entityManager) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = cb.createQuery(entityType);
        final Root<T> allInstances = criteria.from(entityType);
        final CriteriaQuery<T> all = criteria.select(allInstances);
        return entityManager.createQuery(all).getResultList();
    }

    protected Car buildRandomCar() {
        final Car myCar = new Car();
        String regNb = "";
        for (int i = 0; i < new Random().nextInt(12); i++) {
            regNb += new Random().nextInt();
        }
        myCar.setRegistrationNumber(regNb);
        myCar.setConstructor("Lada");
        myCar.setModel("Turbo-61");
        myCar.setNumberOfDoors(new Random().nextInt());
        return myCar;
    }

}
