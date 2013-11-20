package org.bonitasoft.engine.poc.tenant;

import org.hibernate.SessionFactory;

public class Tenant {

    private SessionFactory sessionFactory;

    private ClassLoader classLoader;

    public void stop() {
        sessionFactory = null;
        classLoader = null;
    }

    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
