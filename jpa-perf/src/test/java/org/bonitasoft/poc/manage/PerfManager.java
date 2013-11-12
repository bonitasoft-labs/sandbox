package org.bonitasoft.poc.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class PerfManager {

    private static final int POOL_SIZE = 50;

    private static final int NB_THREADS = 150;

    @Test
    public void mainTest() throws InterruptedException {
        final List<JPAThread> runnables = new ArrayList<JPAThread>();
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < 10; i++) {
            final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory);
            runnables.add(insertEmployeeThread);
            executorService.execute(insertEmployeeThread);
        }
        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        executorService = Executors.newFixedThreadPool(POOL_SIZE);
        final Random random = new Random();
        for (int i = 0; i < NB_THREADS; i++) {
            final int nextInt = random.nextInt(10);
            if (nextInt % 3 == 0) {
                final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory);
                runnables.add(insertEmployeeThread);
                executorService.execute(insertEmployeeThread);
            } else {
                if (nextInt % 2 == 0) {
                    final DeleteEmployeesAddress deleteEmployeesAddress = new DeleteEmployeesAddress(entityManagerFactory);
                    runnables.add(deleteEmployeesAddress);
                    executorService.execute(deleteEmployeesAddress);
                } else {
                    final GetUpdateEmployee updateEmployee = new GetUpdateEmployee(entityManagerFactory);
                    runnables.add(updateEmployee);
                    executorService.execute(updateEmployee);
                }
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        int total = 0;
        int nbInserts = 0;
        int totalInsert = 0;
        int nbUpdates = 0;
        int totalUpdate = 0;
        int nbDeletes = 0;
        int totalDelete = 0;
        int nbErrors = 0;
        for (final JPAThread runnable : runnables) {
            if (!runnable.isCompleted()) {
                nbErrors++;
            } else {
                total += runnable.getDuration();
                if (runnable instanceof InsertEmployeeThread) {
                    nbInserts++;
                    totalInsert += runnable.getDuration();
                }
                if (runnable instanceof DeleteEmployeesAddress) {
                    nbDeletes++;
                    totalDelete += runnable.getDuration();
                } else {
                    nbUpdates++;
                    totalUpdate += runnable.getDuration();
                }
            }
        }
        final double avgDuration = total / runnables.size();
        final double avgInsertDuration = totalInsert / nbInserts;
        final double avgUpdateDuration = totalUpdate / nbUpdates;
        final double avgDeleteDuration = totalDelete / nbDeletes;
        System.out.println("DONE avg=" + avgDuration + " ms");
        System.out.println("\t #inserts=" + nbInserts + ", avg=" + avgInsertDuration + " ms");
        System.out.println("\t #updates=" + nbUpdates + ", avg=" + avgUpdateDuration + " ms");
        System.out.println("\t #delete=" + nbDeletes + ", avg=" + avgDeleteDuration + " ms");
        System.out.println("\t #errors=" + nbErrors);
    }

}
