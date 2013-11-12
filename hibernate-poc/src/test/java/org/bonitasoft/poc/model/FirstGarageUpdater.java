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

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.bonitasoft.poc.util.PersistenceUtil;

/**
 * @author Emmanuel Duchastenier
 */
public class FirstGarageUpdater extends Thread {

    private final CountDownLatch latch;

    private final long garageId;

    private Exception exception;

    /**
     * @param garageId
     *            the Id of the garage to update the
     */
    public FirstGarageUpdater(final long garageId, final CountDownLatch latch) {
        super("FirstCarUpdater");
        this.garageId = garageId;
        this.latch = latch;
    }

    @Override
    public void run() {
        super.run();
        final EntityManagerFactory entityManagerFactory = PersistenceUtil.getInstance().getEntityManagerFactory();
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            try {
                final Garage garage = entityManager.find(Garage.class, garageId);
                if (garage.getCars().isEmpty()) {
                    throw new RuntimeException("Garage with id " + garageId + " should have one car.");
                }
                Car car = garage.getCars().get(0);
                car.setModel("New_car_model_" + UUID.randomUUID().getLeastSignificantBits());
                latch.await();
                transaction.commit();
            } catch (Exception e) {
                exception = e;
                System.err.println(e);
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        } catch (final Exception re) {
            throw new RuntimeException(re);
        } finally {
            entityManager.close();
        }
    }

    public Exception getException() {
        return exception;
    }

}
