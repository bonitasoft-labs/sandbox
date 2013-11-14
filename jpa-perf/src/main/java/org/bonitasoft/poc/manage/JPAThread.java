package org.bonitasoft.poc.manage;

import java.util.List;
import java.util.Random;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

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
		Context context = getTimer().time();
		Context errorContext = null;
	
		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			final EntityManager entityManager = entityManagerFactory.createEntityManager();
			beginTransaction(ut,entityManager,errorContext);
			execute(entityManager);
			commitTransaction(ut,errorContext);
			context.stop();
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}

	protected void commitTransaction(UserTransaction ut, Context errorContext) {
		try {
			ut.commit();
		} catch (SecurityException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			rollbackTransaction(ut, errorContext);
		} catch (IllegalStateException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			rollbackTransaction(ut, errorContext);
		} catch (RollbackException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			if(e.getCause() instanceof OptimisticLockException){
				optimisticLockErrorCounter.inc();
			}
			rollbackTransaction(ut, errorContext);
		} catch (HeuristicMixedException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			rollbackTransaction(ut, errorContext);
		} catch (HeuristicRollbackException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			rollbackTransaction(ut, errorContext);
		} catch (SystemException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
			rollbackTransaction(ut, errorContext);
		}
	}

	protected void beginTransaction(UserTransaction ut, EntityManager entityManager, Context errorContext) {
		try {
			ut.begin();
		} catch (NotSupportedException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
		} catch (SystemException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
		}
		entityManager.joinTransaction();
	}


	private void rollbackTransaction(UserTransaction ut,Context errorContext) {
		try {
			ut.rollback();
		} catch (IllegalStateException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
		} catch (SecurityException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
		} catch (SystemException e) {
			if(errorContext == null){
				errorContext = errorTimer.time();
			}
		}
		errorContext.stop();
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
