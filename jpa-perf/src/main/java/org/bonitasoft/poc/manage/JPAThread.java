package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public abstract class JPAThread implements Runnable {

    private long duration;

    private final EntityManagerFactory entityManagerFactory;

    public JPAThread(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            try {
                execute(entityManager);
                transaction.commit();
            } catch (final RuntimeException re) {
                transaction.rollback();
                throw re;
            }
        } catch (final RuntimeException e) {
            //
        } finally {
            entityManager.close();
            duration = System.currentTimeMillis() - startTime;
        }
    }

    public abstract void execute(EntityManager entityManager);

    public long getDuration() {
        return duration;
    }

}
