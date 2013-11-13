package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

public abstract class JPAThread implements Runnable {

	private final Timer errorTimer;

	private final EntityManagerFactory entityManagerFactory;

	private Counter optimisticLockErrorCounter;

	public JPAThread(final EntityManagerFactory entityManagerFactory,final Timer errorTimer,final Counter optimisticLockErrorCounter) {
		this.entityManagerFactory = entityManagerFactory;
		this.errorTimer = errorTimer;
		this.optimisticLockErrorCounter = optimisticLockErrorCounter;
	}

	@Override
	public void run() {
		boolean status = true;
		Context context = getTimer().time();
		Context errorContext = null;
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			try {
				execute(entityManager);
				transaction.commit();
				incrementCounter();
			} catch (final RollbackException re) {
				errorContext = errorTimer.time();
				if(re.getCause() instanceof OptimisticLockException){
					optimisticLockErrorCounter.inc();
				}
				throw re;
			} catch (final RuntimeException re) {
				if(errorContext == null){
					errorContext = errorTimer.time();
				}
				re.printStackTrace();
				transaction.rollback();
				throw re;
			}
		} catch (final RuntimeException e) {
			incrementErrorCounter();
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			status = false;
			throw e;
		} finally {
			entityManager.close();
			if (!status && errorContext != null) {
				errorContext.stop();
			} else {
				context.stop();
			}
		}
	}


	protected abstract void incrementErrorCounter();

	protected abstract void incrementCounter();

	protected abstract void execute(EntityManager entityManager);

	protected abstract Timer getTimer() ;


}
