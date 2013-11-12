package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class InsertThread extends JPAThread {

    private final AtomicInteger nbInserts;

    private final AtomicLong insertDuration;

	private AtomicInteger nbInsertErrors;

    public InsertThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbInsertErrors, final AtomicLong errorDuration,
            final AtomicInteger nbInserts, final AtomicLong insertDuration) {
        super(entityManagerFactory,errorDuration);
        this.nbInserts = nbInserts;
        this.insertDuration = insertDuration;
        this.nbInsertErrors = nbInsertErrors;
    }

    @Override
    protected void incrementCounter() {
        nbInserts.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        insertDuration.addAndGet(millis);
    }
    
    @Override
    protected void incrementErrorCounter() {
    	nbInsertErrors.getAndIncrement();
    }

}
