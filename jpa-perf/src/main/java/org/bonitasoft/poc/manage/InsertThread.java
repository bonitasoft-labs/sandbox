package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class InsertThread extends JPAThread {

    private final AtomicInteger nbInserts;

    private final AtomicLong insertDuration;

    public InsertThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbErrors, final AtomicLong errorDuration,
            final AtomicInteger nbInserts, final AtomicLong insertDuration) {
        super(entityManagerFactory, nbErrors, errorDuration);
        this.nbInserts = nbInserts;
        this.insertDuration = insertDuration;
    }

    @Override
    protected void incrementCounter() {
        nbInserts.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        insertDuration.addAndGet(millis);
    }

}
