package org.bonitasoft.engine.poc.tenant;

import groovy.lang.GroovyShell;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

public class InsertEntity implements Runnable {

    private final String script;

    private final Tenant tenant;

    public InsertEntity(final Tenant tenant, final String script) {
        this.tenant = tenant;
        this.script = script;
    }

    @Override
    public void run() {
        final ClassLoader previousContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(tenant.getClassLoader());

            final GroovyShell shell = new GroovyShell(tenant.getClassLoader());
            final Object object = shell.evaluate(script);
            System.out.println("returned object from Groovy script: " + object);
            persistEntity(object);
            System.out.println("Object saved to DB: " + object);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(previousContextClassLoader);
        }
    }

    private Serializable persistEntity(final Object entity) throws Exception {
        final SessionFactory sessionFactory = tenant.getSessionFactory();
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            final Serializable saved = session.save(entity);
            tx.commit();
            return saved;
        } catch (final Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

}
