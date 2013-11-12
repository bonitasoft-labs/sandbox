package org.bonitasoft.poc.manage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class PerfManager {

    private static final int POOL_SIZE = 50;

    private static final int NB_THREADS = 1500;

    @Test
    public void mainTest() throws InterruptedException {
        final AtomicInteger nbInserts = new AtomicInteger();
        final AtomicLong insertDuration = new AtomicLong();
        final AtomicInteger nbUpdates = new AtomicInteger();
        final AtomicLong updateDuration = new AtomicLong();
        final AtomicInteger nbDeletes = new AtomicInteger();
        final AtomicLong deleteDuration = new AtomicLong();
        final AtomicInteger nbErrors = new AtomicInteger();
        final AtomicLong errorDuration = new AtomicLong();
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < 10; i++) {
            final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, nbErrors, errorDuration, nbInserts, insertDuration);
            executorService.execute(insertEmployeeThread);
        }
        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        executorService = Executors.newFixedThreadPool(POOL_SIZE);
        final Random random = new Random();
        for (int i = 0; i < NB_THREADS; i++) {
            final int nextInt = random.nextInt(10);
            if (nextInt % 3 == 0) {
                final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, nbErrors, errorDuration, nbInserts,
                        insertDuration);
                executorService.execute(insertEmployeeThread);
            } else {
                if (nextInt % 2 == 0) {
                    final DeleteEmployeesAddress deleteEmployeesAddress = new DeleteEmployeesAddress(entityManagerFactory, nbErrors, errorDuration, nbDeletes,
                            deleteDuration);
                    executorService.execute(deleteEmployeesAddress);
                } else {
                    final GetUpdateEmployee updateEmployee = new GetUpdateEmployee(entityManagerFactory, nbErrors, errorDuration, nbUpdates, updateDuration);
                    executorService.execute(updateEmployee);
                }
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(NB_THREADS * 2, TimeUnit.SECONDS);

        final double avgInsertDuration = insertDuration.get() / nbInserts.get();
        final double avgUpdateDuration = updateDuration.get() / nbUpdates.get();
        final double avgDeleteDuration = deleteDuration.get() / nbDeletes.get();
        final double avgDuration = (insertDuration.get() + updateDuration.get() + deleteDuration.get()) / (nbInserts.get() + nbUpdates.get() + nbDeletes.get());

        System.out.println("DONE avg=" + avgDuration + " ms");
        System.out.println("\t #inserts=" + nbInserts + ", avg=" + avgInsertDuration + " ms");
        System.out.println("\t #updates=" + nbUpdates + ", avg=" + avgUpdateDuration + " ms");
        System.out.println("\t #delete=" + nbDeletes + ", avg=" + avgDeleteDuration + " ms");
        // System.out.println("\t #insert_errors=" + nbInsertErrors);
        // System.out.println("\t #delete_errors=" + nbDeleteErrors);
        // System.out.println("\t #update_errors=" + nbUpdateErrors);
        // System.out.println("\t #other_errors=" + nbOtherErrors);
        System.out.println("\t #errors=" + nbErrors.get());
    }

}
