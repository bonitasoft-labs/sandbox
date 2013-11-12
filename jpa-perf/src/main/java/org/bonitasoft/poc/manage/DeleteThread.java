package org.bonitasoft.poc.manage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;

public abstract class DeleteThread extends JPAThread {

    private final AtomicInteger nbDeletes;

    private final AtomicLong deleteDuration;

    public DeleteThread(final EntityManagerFactory entityManagerFactory, final AtomicInteger nbErrors, final AtomicLong errorDuration,
            final AtomicInteger nbDeletes, final AtomicLong deleteDuration) {
        super(entityManagerFactory, nbErrors, errorDuration);
        this.nbDeletes = nbDeletes;
        this.deleteDuration = deleteDuration;
    }

    @Override
    protected void incrementCounter() {
        nbDeletes.getAndIncrement();
    }

    @Override
    protected void computeDuration(final long millis) {
        deleteDuration.addAndGet(millis);
    }

}
