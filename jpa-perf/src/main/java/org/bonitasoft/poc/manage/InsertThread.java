package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public abstract class InsertThread extends JPAThread {


    private final Timer insertTimer;

	private final Counter insertionErrorCounter;

    public InsertThread(final EntityManagerFactory entityManagerFactory, final MetricRegistry metricRegistry) {
        super(entityManagerFactory,metricRegistry);
        this.insertTimer = metricRegistry.timer("insert-timer");
        this.insertionErrorCounter = metricRegistry.counter("number-of-insertion-errors");
    }


    @Override
    protected Timer getTimer() {
    	return insertTimer;
    }
    
    @Override
    protected void incrementErrorCounter() {
    	insertionErrorCounter.inc();
    }

}
