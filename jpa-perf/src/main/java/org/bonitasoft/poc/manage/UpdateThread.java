package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class UpdateThread extends JPAThread {

    private final AtomicInteger nbUpdates;

    private final AtomicLong updateDuration;

    public UpdateThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbErrors, final AtomicLong errorDuration,
            final AtomicInteger nbUpdates, final AtomicLong updateDuration) {
        super(entityManagerFactory, nbErrors, errorDuration);
        this.nbUpdates = nbUpdates;
        this.updateDuration = updateDuration;
    }

    @Override
    protected void incrementCounter() {
        nbUpdates.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        updateDuration.addAndGet(millis);
    }

}
