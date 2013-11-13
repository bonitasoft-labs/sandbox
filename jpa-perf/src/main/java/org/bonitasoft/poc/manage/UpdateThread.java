package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public abstract class UpdateThread extends JPAThread {

	private final Counter updateCounter;

	private final Timer updateTimer;

	private final Counter updateErrorCounter;

	public UpdateThread(final EntityManagerFactory entityManagerFactory, final Counter updateErrorCounter, final Timer errorTimer,
			final Counter updateCounter, final Timer updateTimer,final Counter nbOptimisticLockError) {
		super(entityManagerFactory, errorTimer,nbOptimisticLockError);
		this.updateCounter = updateCounter;
		this.updateTimer = updateTimer;
		this.updateErrorCounter = updateErrorCounter;
	}

	@Override
	protected void incrementCounter() {
		updateCounter.inc();
	}

	@Override
	protected Timer getTimer() {
		return updateTimer;
	}

	@Override
	protected void incrementErrorCounter() {
		updateErrorCounter.inc();
	}
}
