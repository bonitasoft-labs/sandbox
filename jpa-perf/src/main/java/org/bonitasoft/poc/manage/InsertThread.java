package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public abstract class InsertThread extends JPAThread {

    private final Counter insertionCounter;

    private final Timer insertTimer;

	private final Counter insertionErrorCounter;

    public InsertThread(final EntityManagerFactory entityManagerFactory, final Counter insertionErrorCounter, final Timer errorTimer,
            final Counter insertionCounter, final Timer insertTimer,final Counter optimisticLockErrorCounter) {
        super(entityManagerFactory,errorTimer,optimisticLockErrorCounter,null);
        this.insertionCounter = insertionCounter;
        this.insertTimer = insertTimer;
        this.insertionErrorCounter = insertionErrorCounter;
    }

    @Override
    protected void incrementCounter() {
    	insertionCounter.inc();
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
