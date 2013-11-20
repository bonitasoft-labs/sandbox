/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.poc;

import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * @author Emmanuel Duchastenier
 */
public class PlayWithHiberGojo implements Runnable {

    private final String dir;

    private final String groovyScript;

    /**
     * @param dir
     */
    public PlayWithHiberGojo(final String dir, final String groovyScript) {
        this.dir = dir;
        this.groovyScript = groovyScript;
    }

    @Override
    public void run() {
        ClassLoader previousContextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = null;
        try {
            cl = new URLClassLoader(new URL[] { new URL(dir) }, previousContextClassLoader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().setContextClassLoader(cl);

            GroovyShell shell = new GroovyShell(cl);
            Object object = shell.evaluate(groovyScript);
            System.out.println("returned object from Groovy script: " + object);
            persistEntity(object);
            System.out.println("Object saved to DB: " + object);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(previousContextClassLoader);
        }
    }

    private Serializable persistEntity(final Object entity) throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Serializable saved = session.save(entity);
            tx.commit();
            return saved;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
