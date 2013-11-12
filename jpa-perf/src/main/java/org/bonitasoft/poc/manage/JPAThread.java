package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

public abstract class JPAThread implements Runnable {

    private final AtomicLong errorDuration;

    private final EntityManagerFactory entityManagerFactory;

	private AtomicInteger nbOptimisticLockError;

    public JPAThread(final EntityManagerFactory entityManagerFactory,final AtomicLong errorDuration,final AtomicInteger nbOptimisticLockError) {
        this.entityManagerFactory = entityManagerFactory;
        this.errorDuration = errorDuration;
        this.nbOptimisticLockError = nbOptimisticLockError;
    }

    @Override
    public void run() {
        boolean status = true;
        final long startTime = System.currentTimeMillis();
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            try {
                execute(entityManager);
                transaction.commit();
                incrementCounter();
            } catch (final RollbackException re) {
            	if(re.getCause() instanceof OptimisticLockException){
            		nbOptimisticLockError.getAndIncrement();
            	}
                throw re;
            } catch (final RuntimeException re) {
                re.printStackTrace();
                transaction.rollback();
                throw re;
            }
        } catch (final RuntimeException e) {
        	incrementErrorCounter();
            status = false;
            throw e;
        } finally {
            entityManager.close();
            if (!status) {
                errorDuration.addAndGet(System.currentTimeMillis() - startTime);
            } else {
                computeDuration(System.currentTimeMillis() - startTime);
            }
        }
    }

    protected abstract void computeDuration(long millis);
    
    protected abstract void incrementErrorCounter();

    protected abstract void incrementCounter();

    protected abstract void execute(EntityManager entityManager);

}
