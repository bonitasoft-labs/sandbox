package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public abstract class UpdateThread extends JPAThread {

	private final Timer updateTimer;

	private final Counter updateErrorCounter;

	public UpdateThread(final EntityManagerFactory entityManagerFactory,final MetricRegistry metricRegistry) {
		super(entityManagerFactory,metricRegistry);
		this.updateTimer = metricRegistry.timer("update-timer");
		this.updateErrorCounter = metricRegistry.counter("number-of-update-errors");
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
