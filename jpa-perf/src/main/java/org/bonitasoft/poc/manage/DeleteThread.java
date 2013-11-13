package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public abstract class DeleteThread extends JPAThread {

    private final Timer deleteTimer;

	private Counter deleteErrorCounter;

	public DeleteThread(final EntityManagerFactory entityManagerFactory, final MetricRegistry metricRegistry) {
        super(entityManagerFactory, metricRegistry);
        this.deleteTimer = metricRegistry.timer("delete-timer");
        this.deleteErrorCounter = metricRegistry.counter("number-of-delete-errors");
    }
    
    @Override
    protected Timer getTimer() {
    	return deleteTimer;
    }
    
    @Override
    protected void incrementErrorCounter() {
    	deleteErrorCounter.inc();
    }

}
