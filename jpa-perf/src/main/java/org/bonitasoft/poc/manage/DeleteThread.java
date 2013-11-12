package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class DeleteThread extends JPAThread {

    private final AtomicInteger nbDeletes;

    private final AtomicLong deleteDuration;

	private AtomicInteger nbDeleteErrors;

    public DeleteThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbDeleteErrors, final AtomicLong errorDuration,
            final AtomicInteger nbDeletes, final AtomicLong deleteDuration,final AtomicInteger nbOptimisticLockError) {
        super(entityManagerFactory, errorDuration,nbOptimisticLockError);
        this.nbDeletes = nbDeletes;
        this.deleteDuration = deleteDuration;
        this.nbDeleteErrors = nbDeleteErrors;
    }

    @Override
    protected void incrementCounter() {
        nbDeletes.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        deleteDuration.addAndGet(millis);
    }
    
    @Override
    protected void incrementErrorCounter() {
    	nbDeleteErrors.getAndIncrement();
    }
    

}
