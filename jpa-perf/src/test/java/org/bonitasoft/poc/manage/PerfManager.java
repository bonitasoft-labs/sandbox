package org.bonitasoft.poc.manage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class PerfManager {

	private static final int POOL_SIZE = 50;
	private static final int NB_THREADS = 1500;

	
	@Test
	public void mainTest() throws InterruptedException {
		final MetricRegistry metrics = new MetricRegistry();
		final Counter insertionCounter = metrics.counter("number-of-insertions");
		final Counter updateCounter = metrics.counter("number-of-updates");
		final Counter deleteCounter = metrics.counter("number-of-deletion");
		
		final Counter optimisticLockErrorCounter = metrics.counter("optimistic-lock-errors");
	
		final Timer insertTimer = metrics.timer("insert-timer");
		final Timer updateTimer = metrics.timer("update-timer");
		final Timer deleteTimer = metrics.timer("delete-timer");
		final Timer errorTimer = metrics.timer("error-timer");
		
		final Counter insertionErrorCounter = metrics.counter("number-of-insertion-errors");
		final Counter updateErrorCounter = metrics.counter("number-of-update-errors");
		final Counter deleteErrorCounter = metrics.counter("number-of-delete-errors");
	
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.MILLISECONDS).build();
		
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
		ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

		for (int i = 0; i < 10; i++) {
			final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, insertionErrorCounter, errorTimer, insertionCounter, insertTimer,optimisticLockErrorCounter);
			executorService.execute(insertEmployeeThread);
		}
		executorService.shutdown();
		executorService.awaitTermination(15, TimeUnit.SECONDS);

		executorService = Executors.newFixedThreadPool(POOL_SIZE);
		final Random random = new Random();
		for (int i = 0; i < NB_THREADS; i++) {
			final int nextInt = random.nextInt(10);
			if (nextInt % 5 == 0) {
				final DeleteEmployees deleteEmployees = new DeleteEmployees(entityManagerFactory, deleteErrorCounter, errorTimer, deleteCounter,
						deleteTimer,optimisticLockErrorCounter);
				executorService.execute(deleteEmployees);
			} else  if (nextInt % 3 == 0) {
				final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, insertionErrorCounter, errorTimer, insertionCounter,
						insertTimer,optimisticLockErrorCounter);
				executorService.execute(insertEmployeeThread);
			}else  if (nextInt % 2 == 0) {
				final DeleteEmployeesAddress deleteEmployeesAddress = new DeleteEmployeesAddress(entityManagerFactory, deleteErrorCounter, errorTimer, deleteCounter,
						deleteTimer,optimisticLockErrorCounter);
				executorService.execute(deleteEmployeesAddress);
			} else {
				final GetUpdateEmployee updateEmployee = new GetUpdateEmployee(entityManagerFactory, updateErrorCounter, errorTimer, updateCounter, updateTimer,optimisticLockErrorCounter);
				executorService.execute(updateEmployee);
			}

		}
		executorService.shutdown();
		executorService.awaitTermination(NB_THREADS * 5, TimeUnit.SECONDS);
		
		reporter.report();
	}

}
