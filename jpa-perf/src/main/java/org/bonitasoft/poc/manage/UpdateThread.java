package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class UpdateThread extends JPAThread {

    private final AtomicInteger nbUpdates;

    private final AtomicLong updateDuration;

	private AtomicInteger nbUpdateErrors;

    public UpdateThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbUpdateErrors, final AtomicLong errorDuration,
            final AtomicInteger nbUpdates, final AtomicLong updateDuration) {
        super(entityManagerFactory, errorDuration);
        this.nbUpdates = nbUpdates;
        this.updateDuration = updateDuration;
        this.nbUpdateErrors = nbUpdateErrors;
    }

    @Override
    protected void incrementCounter() {
        nbUpdates.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        updateDuration.addAndGet(millis);
    }

    @Override
    protected void incrementErrorCounter() {
    	nbUpdateErrors.getAndIncrement();
    }
}
