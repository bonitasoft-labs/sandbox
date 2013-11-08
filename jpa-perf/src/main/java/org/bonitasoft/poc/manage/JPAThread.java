package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class JPAThread extends Thread {

    private long time;

    @Override
    public void run() {
        super.run();
        time = System.currentTimeMillis();
        final EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory();
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            execute(entityManager);
            entityManager.getTransaction().commit();
        } catch (final RuntimeException re) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw re;
        } finally {
            entityManager.close();
            time = System.currentTimeMillis() - time;
        }
    }

    public abstract void execute(EntityManager entityManager);

    public long getTime() {
        return time;
    }

}
