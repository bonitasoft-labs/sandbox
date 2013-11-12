package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

public abstract class JPAThread implements Runnable {

    private boolean completed;

    private long duration;

    private final EntityManagerFactory entityManagerFactory;

    public JPAThread(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run() {
        completed = true;
        final long startTime = System.currentTimeMillis();
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            try {
                execute(entityManager);
                transaction.commit();
            } catch (final RollbackException re) {
                throw re;
            } catch (final RuntimeException re) {
                re.printStackTrace();
                transaction.rollback();
                throw re;
            }
        } catch (final RuntimeException e) {
            completed = false;
            throw e;
        } finally {
            duration = System.currentTimeMillis() - startTime;
            entityManager.close();
        }
    }

    public abstract void execute(EntityManager entityManager);

    public long getDuration() {
        return duration;
    }

    public boolean isCompleted() {
        return completed;
    }
}
