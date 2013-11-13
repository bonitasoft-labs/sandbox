package org.bonitasoft.poc.manage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

public class PerfManager {

	private static final int POOL_SIZE = 50;
	private static final int NB_THREADS = 2000;

	
	@Test
	public void mainTest() throws InterruptedException {
		final MetricRegistry metrics = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.MILLISECONDS).build();
		
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
		ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

		for (int i = 0; i < 10; i++) {
			final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory,metrics);
			executorService.execute(insertEmployeeThread);
		}
		executorService.shutdown();
		executorService.awaitTermination(15, TimeUnit.SECONDS);

		executorService = Executors.newFixedThreadPool(POOL_SIZE);
		final Random random = new Random();
		for (int i = 0; i < NB_THREADS; i++) {
			final int nextInt = random.nextInt(10);
			if (nextInt % 5 == 0) {
				final DeleteEmployees deleteEmployees = new DeleteEmployees(entityManagerFactory, metrics);
				executorService.execute(deleteEmployees);
			} else  if (nextInt % 3 == 0) {
				final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, metrics);
				executorService.execute(insertEmployeeThread);
			}else  if (nextInt % 2 == 0) {
				final DeleteEmployeesAddress deleteEmployeesAddress = new DeleteEmployeesAddress(entityManagerFactory, metrics);
				executorService.execute(deleteEmployeesAddress);
			} else {
				final JPAThread updateEmployee = new GetUpdateEmployee(entityManagerFactory, metrics);
				executorService.execute(updateEmployee);
			}

		}
		executorService.shutdown();
		executorService.awaitTermination(NB_THREADS * 5, TimeUnit.SECONDS);
		
		reporter.report();
	}

}
