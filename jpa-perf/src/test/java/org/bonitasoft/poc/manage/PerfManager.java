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
		final AtomicInteger nbInsertErrors = new AtomicInteger();
		final AtomicInteger nbUpdateErrors = new AtomicInteger();
		final AtomicInteger nbDeleteErrors = new AtomicInteger();
		final AtomicLong errorDuration = new AtomicLong();
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
		ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

		for (int i = 0; i < 10; i++) {
			final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, nbInsertErrors, errorDuration, nbInserts, insertDuration);
			executorService.execute(insertEmployeeThread);
		}
		executorService.shutdown();
		executorService.awaitTermination(15, TimeUnit.SECONDS);

		executorService = Executors.newFixedThreadPool(POOL_SIZE);
		final Random random = new Random();
		for (int i = 0; i < NB_THREADS; i++) {
			final int nextInt = random.nextInt(10);
			if (nextInt % 5 == 0) {
				final DeleteEmployees deleteEmployees = new DeleteEmployees(entityManagerFactory, nbDeleteErrors, errorDuration, nbDeletes,
						deleteDuration);
				executorService.execute(deleteEmployees);
			} else  if (nextInt % 3 == 0) {
				final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, nbInsertErrors, errorDuration, nbInserts,
						insertDuration);
				executorService.execute(insertEmployeeThread);
			}else  if (nextInt % 2 == 0) {
				final DeleteEmployeesAddress deleteEmployeesAddress = new DeleteEmployeesAddress(entityManagerFactory, nbDeleteErrors, errorDuration, nbDeletes,
						deleteDuration);
				executorService.execute(deleteEmployeesAddress);
			} else {
				final GetUpdateEmployee updateEmployee = new GetUpdateEmployee(entityManagerFactory, nbUpdateErrors, errorDuration, nbUpdates, updateDuration);
				executorService.execute(updateEmployee);
			}

		}
		executorService.shutdown();
		executorService.awaitTermination(NB_THREADS * 5, TimeUnit.SECONDS);

		final double avgInsertDuration = insertDuration.get() / nbInserts.get();
		final double avgUpdateDuration = updateDuration.get() / nbUpdates.get();
		final double avgDeleteDuration = deleteDuration.get() / nbDeletes.get();
		final double avgDuration = (insertDuration.get() + updateDuration.get() + deleteDuration.get()) / (nbInserts.get() + nbUpdates.get() + nbDeletes.get());

		System.out.println("DONE \tavg=" + avgDuration + " ms");
		System.out.println("\t #inserts=" + nbInserts + ", avg=" + avgInsertDuration + " ms");
		System.out.println("\t #inserts_errors=" + nbInsertErrors.get());
		System.out.println("\t #updates=" + nbUpdates + ", avg=" + avgUpdateDuration + " ms");
		System.out.println("\t #update_errors=" + nbUpdateErrors.get());
		System.out.println("\t #delete=" + nbDeletes + ", avg=" + avgDeleteDuration + " ms");
		System.out.println("\t #delete_errors=" + nbDeleteErrors.get());
		int totalExecutions = nbInserts.get() + nbUpdates.get() + nbDeletes.get() + nbDeleteErrors.get() + nbInsertErrors.get() + nbUpdateErrors.get();
		System.out.println("\t #total_exec=" + totalExecutions);

	}

}
