package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {

    private static class SingletonHolder {

        public static final EntityManagerFactorySingleton INSTANCE = new EntityManagerFactorySingleton();
    }

    private final EntityManagerFactory entityManagerFactory;

    private EntityManagerFactorySingleton() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
        } catch (final Throwable cause) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial entityManagerFactory creation failed." + cause);
            throw new ExceptionInInitializerError(cause);
        }
    }

    public static EntityManagerFactorySingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

}
