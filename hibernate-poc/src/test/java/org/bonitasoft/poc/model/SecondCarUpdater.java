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
public class SecondCarUpdater extends Thread {

    private final CountDownLatch latch;

    private final String registrationNumber;

    /**
     * @param carId
     *            the Id of the car to update
     */
    public SecondCarUpdater(final String carId, final CountDownLatch latch) {
        super("SecondCarUpdater");
        registrationNumber = carId;
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
                final Car car = entityManager.find(Car.class, registrationNumber);
                car.setModel("PLT_" + UUID.randomUUID().getLeastSignificantBits());
                transaction.commit();
                latch.countDown();
            } catch (Exception e) {
                System.err.println(e);
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        } catch (final RuntimeException re) {
            throw re;
        } finally {
            entityManager.close();
        }
    }

}
