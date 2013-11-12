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

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

/**
 * @author Emmanuel Duchastenier
 */
public class ConcurrencyTest extends AbstractTest {

    @Override
    public List<Class<?>> getEntityTypesToCleanAfterTest() {
        return Arrays.asList(Garage.class, Car.class);
    }

    @Test(expected = PersistenceException.class)
    public void outdatedModificationThrowsException_sameThread() throws Exception {
        final Car car = buildRandomCar();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(car);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        // retrieve the car a first time:
        final EntityManager entityManager1 = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Car foundCar = entityManager1.find(Car.class, car.getRegistrationNumber());
        foundCar.setModel("toto");

        // retrieve the car a first time:
        final EntityManager entityManager2 = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Car sameCar = entityManager2.find(Car.class, car.getRegistrationNumber());
        sameCar.setConstructor("TutTut-Pouet");
        persistenceUtil.closeTransactionAndEntityManager(entityManager2);

        try {
            // Try to commit the first open transaction afterwards:
            final EntityTransaction transaction = entityManager1.getTransaction();
            if (transaction.isActive()) {
                transaction.commit();
            }
            fail("Concurrent modification of Car should be forbidden by Optimistic locking");
        } finally {
            entityManager1.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void two_threads_concurrently_modifying_same_car_throws_exception() throws Exception {
        // store a Car in DB:
        Car car = buildRandomCar();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(car);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        CountDownLatch latch = new CountDownLatch(1);
        final FirstCarUpdater carUpdater1 = new FirstCarUpdater(car.getRegistrationNumber(), latch);
        final SecondCarUpdater carUpdater2 = new SecondCarUpdater(car.getRegistrationNumber(), latch);
        carUpdater1.start();
        carUpdater2.start();

        // wait for threads to finish:
        carUpdater1.join();
        carUpdater2.join();

        throw carUpdater1.getException();
    }

    @Test(expected = PersistenceException.class)
    public void two_threads_concurrently_modifying_same_garage_with_some_cars_throws_exception() throws Exception {
        final Garage myGarage = new Garage();
        myGarage.setName("Garage Marcel");
        myGarage.addToCars(buildRandomCar());
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myGarage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        CountDownLatch latch = new CountDownLatch(1);
        final FirstGarageUpdater garageUpdater1 = new FirstGarageUpdater(myGarage.getId(), latch);
        final SecondGarageUpdater garageUpdater2 = new SecondGarageUpdater(myGarage.getId(), latch);
        garageUpdater1.start();
        garageUpdater2.start();

        // wait for threads to finish:
        garageUpdater1.join();
        garageUpdater2.join();

        throw garageUpdater1.getException();

    }

}
