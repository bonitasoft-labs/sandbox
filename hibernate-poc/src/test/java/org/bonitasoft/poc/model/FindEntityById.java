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
package org.bonitasoft.poc.model;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.util.PersistenceUtil;

/**
 * @author Emmanuel Duchastenier
 */
public class FindEntityById<T> implements Runnable {

    private final Class<T> typeParameterClass;

    private final Object entityId;

    private T foundEntity;

    public FindEntityById(final Class<T> typeParameterClass, final Object entityId) {
        this.typeParameterClass = typeParameterClass;
        this.entityId = entityId;
    }

    @Override
    public void run() {
        PersistenceUtil persistUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistUtil.createEntityManagerAndBeginTransaction();
        foundEntity = entityManager.find(typeParameterClass, entityId);
        persistUtil.closeTransactionAndEntityManager(entityManager);
    }

    /**
     * @return the entity
     */
    public T getEntity() {
        return foundEntity;
    }
}
