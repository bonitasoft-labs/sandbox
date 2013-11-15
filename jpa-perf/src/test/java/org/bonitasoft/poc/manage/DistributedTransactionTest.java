package org.bonitasoft.poc.manage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.bonitasoft.poc.model.Car;
import org.bonitasoft.poc.model.Employee;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

public class DistributedTransactionTest {

    private static PoolingDataSource ds1;

    private static PoolingDataSource ds2;

    @BeforeClass
    public static void createDatasource() throws NamingException, SQLException {
        // initialisation du contexte
        // System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.bonitasoft.poc.manage.SimpleMemoryContextFactory");

        ds1 = new PoolingDataSource();
        ds1.setUniqueName("java:/comp/env/jdbc/PGDS1");
        ds1.setClassName("org.postgresql.xa.PGXADataSource");
        ds1.setMaxPoolSize(3);
        ds1.setAllowLocalTransactions(true);
        ds1.getDriverProperties().put("serverName", "192.168.1.212");
        ds1.getDriverProperties().put("databaseName", "PerfDB");
        ds1.getDriverProperties().put("user", "postgres");
        ds1.getDriverProperties().put("password", "admin");
        ds1.init();

        ds2 = new PoolingDataSource();
        ds2.setUniqueName("java:/comp/env/jdbc/PGDS2");
        ds2.setClassName("org.postgresql.xa.PGXADataSource");
        ds2.setMaxPoolSize(3);
        ds2.setAllowLocalTransactions(true);
        ds2.getDriverProperties().put("serverName", "192.168.1.212");
        ds2.getDriverProperties().put("databaseName", "PerfDB2");
        ds2.getDriverProperties().put("user", "postgres");
        ds2.getDriverProperties().put("password", "admin");
        ds2.init();

        final Map<String, Object> mappings = new HashMap<String, Object>();
        mappings.put("java:/comp/env/jdbc/PGDS1", ds1);
        mappings.put("java:/comp/env/jdbc/PGDS2", ds2);
        mappings.put("java:comp/UserTransaction", TransactionManagerServices.getTransactionManager());

        final InitialContext ctx = new InitialContext();
        final MemoryJNDISetup jndiSetup = new MemoryJNDISetup(ctx, mappings);
        jndiSetup.init();
    }

    @AfterClass
    public static void closeDataSource() {
        ds1.close();
        ds2.close();
    }

    @Test
    public void jtaTransactionWithMultipleDatasource() throws InterruptedException, NamingException, SecurityException, IllegalStateException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
        final EntityManagerFactory entityManagerFactoryDS1 = Persistence.createEntityManagerFactory("PostgresPersistenceUnit1");
        final EntityManagerFactory entityManagerFactoryDS2 = Persistence.createEntityManagerFactory("PostgresPersistenceUnit2");

        final UserTransaction ut = getUserTransaction();
        ut.begin();
        EntityManager ds1EM = entityManagerFactoryDS1.createEntityManager();
        ds1EM.joinTransaction();
        final Employee e = new Employee();
        e.setName("Romain");
        e.setCreated(new Date());
        e.setAge(28);
        ds1EM.persist(e);

        EntityManager ds2EM = entityManagerFactoryDS2.createEntityManager();
        ds2EM.joinTransaction();
        final Employee e2 = new Employee();
        e2.setName("Matthieu");
        e2.setCreated(new Date());
        e2.setAge(30);
        ds2EM.persist(e2);
        ut.commit();
        ut.begin();
        try {
            ds1EM = entityManagerFactoryDS1.createEntityManager();
            TypedQuery<Employee> query = ds1EM.createQuery("FROM Employee e WHERE e.name = 'Romain'", Employee.class);
            assertEquals("Too many entry in PGDS1", query.getResultList().size(), 1);
            assertEquals("Invalid employee name in PGDS1", query.getResultList().get(0).getName(), "Romain");

            ds2EM = entityManagerFactoryDS2.createEntityManager();
            query = ds2EM.createQuery("FROM Employee e WHERE e.name = 'Matthieu'", Employee.class);
            assertEquals("Too many entry in PGDS2", query.getResultList().size(), 1);
            assertEquals("Invalid employee name in PGDS2", query.getSingleResult().getName(), "Matthieu");
        } finally {
            ut.commit();
        }
    }

    @Test
    public void jtaTransactionRollbackedWithMultipleDatasource() throws InterruptedException, NamingException, SecurityException, IllegalStateException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
        final EntityManagerFactory entityManagerFactoryDS1 = Persistence.createEntityManagerFactory("PostgresPersistenceUnit1");
        final EntityManagerFactory entityManagerFactoryDS2 = Persistence.createEntityManagerFactory("PostgresPersistenceUnit2");

        final UserTransaction ut = getUserTransaction();
        ut.begin();
        final EntityManager ds1EM0 = entityManagerFactoryDS1.createEntityManager();
        ds1EM0.joinTransaction();
        final Car e = new Car();
        e.setRegistrationNumber("45YD45");
        e.setConstructor("Merco");
        ds1EM0.persist(e);
        ut.commit();

        // retrieve the car a first time:
        ut.begin();
        EntityManager ds1EM = entityManagerFactoryDS1.createEntityManager();
        final Car foundCar = ds1EM.find(Car.class, e.getRegistrationNumber());
        foundCar.setModel("toto");

        ds1EM = entityManagerFactoryDS1.createEntityManager();
        final Car sameCar = ds1EM.find(Car.class, e.getRegistrationNumber());
        sameCar.setModel("tata");

        EntityManager ds2EM = entityManagerFactoryDS2.createEntityManager();
        ds2EM.joinTransaction();
        final Employee e2 = new Employee();
        e2.setName("Matthieu");
        e2.setCreated(new Date());
        e2.setAge(30);
        ds2EM.persist(e2);
        try {
            ut.commit();
            fail("Commit should fail");
        } catch (final Exception ex) {
            ut.begin();
            ds1EM = entityManagerFactoryDS1.createEntityManager();
            final TypedQuery<Car> query = ds1EM.createQuery("FROM Car e WHERE e.registrationNumber = '45YD45'", Car.class);
            assertEquals("Too many entry in PGDS1", query.getResultList().size(), 1);
            assertEquals("Invalid car model in PGDS1", query.getResultList().get(0).getModel(), null);

            ds2EM = entityManagerFactoryDS2.createEntityManager();
            final TypedQuery<Employee> query2 = ds2EM.createQuery("FROM Employee e WHERE e.name = 'Matthieu'", Employee.class);
            assertEquals("Too many entry in PGDS2", query2.getResultList().size(), 0);
            ut.commit();
        }
    }

    protected UserTransaction getUserTransaction() throws NamingException {
        final InitialContext ctx = new InitialContext();
        return (UserTransaction) ctx.lookup("java:comp/UserTransaction");
    }

}
