package org.bonitasoft.poc.manage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jndi.JndiTemplate;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

public class PerfManager {

    private static final int POOL_SIZE = 50;

    private static final int NB_THREADS = 2000;

    private static PoolingDataSource ds1;

    @BeforeClass
    public static void createDatasource() throws NamingException, SQLException {
        // initialisation du contexte
        // System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.bonitasoft.poc.manage.SimpleMemoryContextFactory");

        ds1 = new PoolingDataSource();
        ds1.setUniqueName("java:/comp/env/jdbc/DefaultDS");
        ds1.setClassName("org.postgresql.xa.PGXADataSource");
        ds1.setMaxPoolSize(3);
        ds1.setAllowLocalTransactions(true);
        ds1.getDriverProperties().put("serverName", "192.168.1.212");
        ds1.getDriverProperties().put("databaseName", "PerfDB");
        ds1.getDriverProperties().put("user", "postgres");
        ds1.getDriverProperties().put("password", "admin");
        ds1.init();

        final Map<String, Object> mappings = new HashMap<String, Object>();
        mappings.put("java:/comp/env/jdbc/DefaultDS", ds1);
        mappings.put("java:comp/UserTransaction", TransactionManagerServices.getTransactionManager());

        final JndiTemplate jndiTemplate = new JndiTemplate();
        final MemoryJNDISetup jndiSetup = new MemoryJNDISetup(jndiTemplate, mappings);
        jndiSetup.init();
    }

    @AfterClass
    public static void closeDataSource() {
        ds1.close();
    }

    @Test
    public void mainTest() throws InterruptedException {
        final MetricRegistry metrics = new MetricRegistry();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.MILLISECONDS)
                .build();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PostgresPersistenceUnit");
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < 10; i++) {
            final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, metrics);
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
            } else if (nextInt % 3 == 0) {
                final InsertEmployeeThread insertEmployeeThread = new InsertEmployeeThread(entityManagerFactory, metrics);
                executorService.execute(insertEmployeeThread);
            } else if (nextInt % 2 == 0) {
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
