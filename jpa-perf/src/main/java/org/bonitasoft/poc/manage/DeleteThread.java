package org.bonitasoft.poc.manage;

import javax.persistence.EntityManagerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public abstract class DeleteThread extends JPAThread {

    private final Counter deleteCounter;

    private final Timer deleteTimer;

	private Counter deleteErrorCounter;

    public DeleteThread(final EntityManagerFactory entityManagerFactory, final Counter deleteErrorCounter, final Timer errorDuration,
            final Counter deleteCounter, final Timer deleteTimer,final Counter nbOptimisticLockError) {
        super(entityManagerFactory, errorDuration,nbOptimisticLockError);
        this.deleteCounter = deleteCounter;
        this.deleteTimer = deleteTimer;
        this.deleteErrorCounter = deleteErrorCounter;
    }

    @Override
    protected void incrementCounter() {
    	deleteCounter.inc();
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
