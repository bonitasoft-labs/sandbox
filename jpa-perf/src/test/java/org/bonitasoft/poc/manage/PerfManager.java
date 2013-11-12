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

    @Test
    public void mainTest() throws InterruptedException {
        final List<JPAThread> runnables = new ArrayList<JPAThread>();
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 10; i++) {
            final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory);
            runnables.add(insertEmployeeThread);
            executorService.execute(insertEmployeeThread);
        }
        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        executorService = Executors.newFixedThreadPool(20);
        final Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final int nextInt = random.nextInt(10);
            if (nextInt % 3 == 0) {
                final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory);
                runnables.add(insertEmployeeThread);
                executorService.execute(insertEmployeeThread);
            } else {
                // if (nextInt % 2 == 0) {
                final GetUpdateEmployee updateEmployee = new GetUpdateEmployee(entityManagerFactory);
                runnables.add(updateEmployee);
                executorService.execute(updateEmployee);
                // } else {
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        int total = 0;
        int nbInserts = 0;
        int totalInsert = 0;
        int nbUpdates = 0;
        int totalUpdate = 0;
        for (final JPAThread runnable : runnables) {
            total += runnable.getDuration();
            if (runnable instanceof InsertEmployeeThread) {
                nbInserts++;
                totalInsert += runnable.getDuration();
            } else {
                nbUpdates++;
                totalUpdate += runnable.getDuration();
            }
        }
        final double avgDuration = total / runnables.size();
        final double avgInsertDuration = totalInsert / nbInserts;
        final double avgUpdateDuration = totalUpdate / nbUpdates;
        System.out.println("DONE avg=" + avgDuration + " ms");
        System.out.println("\t #inserts=" + nbInserts + ", avg=" + avgInsertDuration + " ms");
        System.out.println("\t #updates=" + nbUpdates + ", avg=" + avgUpdateDuration + " ms");
    }

}
