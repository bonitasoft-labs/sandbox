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

    private final Counter optimisticLockErrorCounter;

    private final Random random;

    protected Counter employeeNotFoundCounter;

    public JPAThread(final EntityManagerFactory entityManagerFactory, final MetricRegistry metricRegisrty) {
        random = new Random();
        this.entityManagerFactory = entityManagerFactory;
        errorTimer = metricRegisrty.timer("error-timer");
        optimisticLockErrorCounter = metricRegisrty.counter("optimistic-lock-errors");
        employeeNotFoundCounter = metricRegisrty.counter("employee-not-found-counter");
    }

    @Override
    public void run() {
        final Context context = getTimer().time();
        final Context errorContext = null;
        try {
            final UserTransaction ut = getUserTransaction();
            final EntityManager entityManager = getEntityManager();
            beginTransaction(ut, entityManager, errorContext);
            execute(entityManager);
            commitTransaction(ut, errorContext);
            context.stop();
        } catch (final NamingException e1) {
            e1.printStackTrace();
        }
    }

	protected EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	protected UserTransaction getUserTransaction() throws NamingException {
		InitialContext ctx = new InitialContext();
		final UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
		return ut;
	}

    protected void commitTransaction(final UserTransaction ut, Context errorContext) {
        try {
            ut.commit();
        } catch (final SecurityException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            rollbackTransaction(ut, errorContext);
        } catch (final IllegalStateException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            rollbackTransaction(ut, errorContext);
        } catch (final RollbackException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            if (e.getCause() instanceof OptimisticLockException) {
                optimisticLockErrorCounter.inc();
            }
            rollbackTransaction(ut, errorContext);
        } catch (final HeuristicMixedException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            rollbackTransaction(ut, errorContext);
        } catch (final HeuristicRollbackException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            rollbackTransaction(ut, errorContext);
        } catch (final SystemException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
            rollbackTransaction(ut, errorContext);
        }
    }

    protected void beginTransaction(final UserTransaction ut, final EntityManager entityManager, Context errorContext) {
        try {
            ut.begin();
        } catch (final NotSupportedException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
        } catch (final SystemException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
        }
        entityManager.joinTransaction();
    }

    private void rollbackTransaction(final UserTransaction ut, Context errorContext) {
        try {
            ut.rollback();
        } catch (final IllegalStateException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
        } catch (final SecurityException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
        } catch (final SystemException e) {
            if (errorContext == null) {
                errorContext = errorTimer.time();
            }
        }
        errorContext.stop();
    }

    protected abstract void incrementErrorCounter();

    protected abstract void execute(EntityManager entityManager);

    protected abstract Timer getTimer();

    protected Random getRandom() {
        return random;
    }

    protected Employee findRandomEmployee(final EntityManager entityManager) {
        final Random random = getRandom();
        final int randomAge1 = random.nextInt(42) + 20;
        final int randomAge2 = random.nextInt(42) + 20;
        final int max = Math.max(randomAge1, randomAge2);
        final int min = Math.min(randomAge1, randomAge2);
        final StringBuilder builder = new StringBuilder("FROM Employee WHERE name LIKE 'Matti%' AND age > " + min + " AND age < " + max + " ORDER BY name ");
        if (random.nextInt() % 2 == 0) {
            builder.append(" ASC");
        } else {
            builder.append(" DESC");
        }
        final TypedQuery<Employee> query = entityManager.createQuery(builder.toString(), Employee.class);
        query.setFirstResult(0);
        query.setMaxResults(1);
        final List<Employee> employees = query.getResultList();
        if (employees.isEmpty()) {
            employeeNotFoundCounter.inc();
            return null;
        }
        return employees.get(0);
    }

}
