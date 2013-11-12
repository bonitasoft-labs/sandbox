/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.poc.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Romain Bioteau
 */
public class PersistenceUtil {

    private static PersistenceUtil INSTANCE;

    public static synchronized PersistenceUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersistenceUtil();
        }
        return INSTANCE;
    }

    private final EntityManagerFactory entityFactory;

    PersistenceUtil() {
        entityFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
    }

    public void closeTransactionAndEntityManager(final EntityManager entityManager) {
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.commit();
            entityManager.close();
        } catch (final RuntimeException re) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            throw re;
        }
    }

    public EntityManager createEntityManagerAndBeginTransaction() {
        final EntityManagerFactory managerFactory = getEntityManagerFactory();
        final EntityManager entityManager = managerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityFactory;
    }

}
