package org.bonitasoft.poc.manage;

import java.util.List;
import java.util.Random;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import javax.transaction.RollbackException;
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
        Context errorContext = null;
        try {
            final InitialContext ctx = new InitialContext();
            final UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            errorContext = beginTransaction(ut, entityManager);
            if (errorContext == null) {
                execute(entityManager);
                errorContext = commitTransaction(ut);
            }
            context.stop();
        } catch (final NamingException ne) {

        } finally {
            if (errorContext != null) {
                errorContext.stop();
            }
        }
    }

    protected Context commitTransaction(final UserTransaction ut) {
        try {
            ut.commit();
            return null;
        } catch (final RollbackException e) {
            if (e.getCause() instanceof OptimisticLockException) {
                optimisticLockErrorCounter.inc();
            }
            return rollbackTransaction(ut);
        } catch (final Exception e) {
            return rollbackTransaction(ut);
        }
    }

    protected Context beginTransaction(final UserTransaction ut, final EntityManager entityManager) {
        try {
            ut.begin();
            entityManager.joinTransaction();
            return null;
        } catch (final Exception e) {
            incrementErrorCounter();
            return errorTimer.time();
        }
    }

    private Context rollbackTransaction(final UserTransaction ut) {
        incrementErrorCounter();
        try {
            ut.rollback();
        } catch (final Exception e) {

        }
        return errorTimer.time();
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
