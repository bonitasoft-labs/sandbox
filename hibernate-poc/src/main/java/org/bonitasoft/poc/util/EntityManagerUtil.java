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
import javax.persistence.Persistence;

/**
 * @author Romain Bioteau
 */
public class EntityManagerUtil {

    private static EntityManagerUtil INSTANCE;

    public static synchronized EntityManagerUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntityManagerUtil();
        }
        return INSTANCE;
    }

    private final EntityManagerFactory entityFactory;

    EntityManagerUtil() {
        entityFactory = Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
    }

    public EntityManager createEntityManager() {
        final EntityManagerFactory managerFactory = getEntityManagerFactory();
        return managerFactory.createEntityManager();
    }
    
    // create new entityManagerFactory every time to enable hibernate create-drop
    public EntityManagerFactory getEntityManagerFactory() {
        if (entityFactory != null && entityFactory.isOpen()) {
            entityFactory.close();
        }
        return Persistence.createEntityManagerFactory("DefaultPersistenceUnit");
    }

}
