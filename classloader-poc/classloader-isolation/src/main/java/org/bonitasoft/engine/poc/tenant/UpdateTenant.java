package org.bonitasoft.engine.poc.tenant;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UpdateTenant implements Runnable {

    private final String dir;

    private final Tenant tenant;

    public UpdateTenant(final String dir, final Tenant tenant) {
        this.dir = dir;
        this.tenant = tenant;
    }

    @Override
    public void run() {
        final ClassLoader previousContextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = null;
        try {
            cl = new URLClassLoader(new URL[] { new URL(dir) }, previousContextClassLoader);
        } catch (final MalformedURLException e1) {
            e1.printStackTrace();
        }
        tenant.setClassLoader(cl);

        try {
            Thread.currentThread().setContextClassLoader(cl);
            final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            tenant.setSessionFactory(sessionFactory);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(previousContextClassLoader);
        }
    }

    public Tenant getTenant() {
        return tenant;
    }

}
