package org.bonitasoft.poc.manage;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

public abstract class JPAThread implements Runnable {

	private final Timer errorTimer;
	private final EntityManagerFactory entityManagerFactory;
	private Counter optimisticLockErrorCounter;
	private Random random;
	protected Counter employeeNotFoundCounter;

	public JPAThread(final EntityManagerFactory entityManagerFactory,final MetricRegistry metricRegisrty) {
		this.random = new Random();
		this.entityManagerFactory = entityManagerFactory;
		this.errorTimer = metricRegisrty.timer("error-timer");
		this.optimisticLockErrorCounter = metricRegisrty.counter("optimistic-lock-errors");
		this.employeeNotFoundCounter = metricRegisrty.counter("employee-not-found-counter");
	}

	@Override
	public void run() {
		boolean status = true;
		Context context = getTimer().time();
		Context errorContext = null;
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			try {
				execute(entityManager);
				transaction.commit();
			} catch (final RollbackException re) {
				errorContext = errorTimer.time();
				if(re.getCause() instanceof OptimisticLockException){
					optimisticLockErrorCounter.inc();
				}
				throw re;
			} catch (final RuntimeException re) {
				if(errorContext == null){
					errorContext = errorTimer.time();
				}
				re.printStackTrace();
				transaction.rollback();
				throw re;
			}
		} catch (final RuntimeException e) {
			incrementErrorCounter();
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			status = false;
			throw e;
		} finally {
			entityManager.close();
			if (!status && errorContext != null) {
				errorContext.stop();
			} else {
				context.stop();
			}
		}
	}


	protected abstract void incrementErrorCounter();

	protected abstract void execute(EntityManager entityManager);

	protected abstract Timer getTimer() ;
	
	protected Random getRandom() {
		return random;
	}

	protected Employee findRandomEmployee(final EntityManager entityManager) {
		Random random = getRandom();
		int randomAge1 = random.nextInt(42)+20;
		int randomAge2 = random.nextInt(42)+20 ;
		int max = Math.max(randomAge1, randomAge2);
		int min = Math.min(randomAge1, randomAge2);
		final StringBuilder builder = new StringBuilder("FROM Employee WHERE name LIKE 'Matti%' AND age > "+min+" AND age < "+max+" ORDER BY name ");
		if (random.nextInt() % 2 == 0) {
			builder.append(" ASC");
		} else {
			builder.append(" DESC");
		}
		final TypedQuery<Employee> query = entityManager.createQuery(builder.toString(),Employee.class);
		query.setFirstResult(0);
		query.setMaxResults(1);
		final List<Employee> employees = query.getResultList();
		if(employees.isEmpty()){
			employeeNotFoundCounter.inc();
			return null;
		}
		return employees.get(0);
	}

}
