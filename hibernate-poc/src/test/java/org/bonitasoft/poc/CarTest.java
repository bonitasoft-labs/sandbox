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
package org.bonitasoft.poc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.bonitasoft.poc.model.Car;
import org.bonitasoft.poc.model.Person;
import org.bonitasoft.poc.util.PersistenceUtil;
import org.junit.After;
import org.junit.Test;

/**
 * @author Romain Bioteau
 * @author Emmanuel Duchastenier
 * @author Matthieu Chaffotte
 */
public class CarTest {

    private static PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();


	@After
	public void clearDatabase(){
		deleteEntity(Person.class);
		deleteEntity(Car.class);
	}

	protected <T> void deleteEntity(Class<T> entityType) {
		PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
		EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(entityType);
		Root<T> allInstances = criteria.from(entityType);
		CriteriaQuery<T> all = criteria.select(allInstances);
		for( Object car : entityManager.createQuery(all).getResultList()){
			entityManager.remove(car);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}

    @Test(expected = PersistenceException.class)
    public void outdatedModificationThrowsException() throws Exception {
        Car car = buildRandomCar();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(car);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        // retrieve the car a first time:
        final EntityManager entityManager1 = persistenceUtil.createEntityManagerAndBeginTransaction();
        Car foundCar = entityManager1.find(Car.class, car.getRegistrationNumber());
        foundCar.setModel("toto");

        // retrieve the car a first time:
        final EntityManager entityManager2 = persistenceUtil.createEntityManagerAndBeginTransaction();
        Car sameCar = entityManager2.find(Car.class, car.getRegistrationNumber());
        sameCar.setConstructor("TutTut-Pouet");
        persistenceUtil.closeTransactionAndEntityManager(entityManager2);

        try {
            // Try to commit the first open transaction afterwards:
            EntityTransaction transaction = entityManager1.getTransaction();
            if (transaction.isActive()) {
                transaction.commit();
            }
            fail("Concurrent modification of Car should be forbidden by Optimistic locking");
        } finally {
            entityManager1.close();
        }
    }

    private Car buildRandomCar() {
        Car myCar = new Car();
        String regNb = "";
        for (int i = 0; i < new Random().nextInt(12); i++) {
            regNb += new Random().nextInt();
        }
        myCar.setRegistrationNumber(regNb);
        myCar.setConstructor("Lada");
        myCar.setModel("Turbo-61");
        myCar.setNumberOfDoors(new Random().nextInt());
        return myCar;
    }

    @Test
    public void storeAndGetCar() {
        Car myCar = new Car();
        myCar.setRegistrationNumber("316DJV38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals(foundCar, myCar);
    }

    @Test
    public void updateCar() {
        Car myCar = new Car();
        myCar.setRegistrationNumber("45DV38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        foundCar.setConstructor("Mercedes-Benz");
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals("Update failed", "Mercedes-Benz", foundCar.getConstructor());
    }

    @Test
    public void deleteCar() {
        Car myCar = new Car();
        myCar.setRegistrationNumber("145AA38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        entityManager.remove(foundCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNull(foundCar);
    }

    @Test
    public void findCarByConstructor() {
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        for (int i = 0; i < 10; i++) {
            Car myCar = new Car();
            myCar.setRegistrationNumber(UUID.randomUUID().toString());
            if (i % 3 == 0) {
                myCar.setConstructor("Mercedes");
            } else {
                myCar.setConstructor("Porsche");
            }
            myCar.setModel("SLR-500");
            myCar.setNumberOfDoors(3);
            entityManager.persist(myCar);
        }
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> criteria = cb.createQuery(Car.class);
        Root<Car> cars = criteria.from(Car.class);
        criteria.where(cb.equal(cars.get("constructor"), "Mercedes"));
        TypedQuery<Car> constructorQuery = entityManager.createQuery(criteria);
        constructorQuery.setFirstResult(0);
        constructorQuery.setMaxResults(20);
        List<Car> result = constructorQuery.getResultList();
        assertEquals("Invalid number of Mercedes", 4, result.size());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }


	@Test(expected=PersistenceException.class)
	public void notNullConstraint() {
		Car myCar = new Car();
		myCar.setRegistrationNumber("145AA38");
		myCar.setConstructor(null);
		myCar.setModel("SLR-500");
		myCar.setNumberOfDoors(3);

		PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
		EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
		try{
			entityManager.persist(myCar);
			fail("Car constructor is null, car cannot be persisted");
		}finally{
			entityManager.close();
		}
	}
	
	@Test
	public void aCarBelongsToAPerson() {
		Car myCar = new Car();
		myCar.setRegistrationNumber("145AA38");
		myCar.setConstructor("Mercedes");
		myCar.setModel("SLR-500");
		myCar.setNumberOfDoors(3);
		
		Person romain = new Person();
		romain.setFirstName("Romain");
		romain.setLastName("Bioteau");
		romain.setCar(myCar);
	
		PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
		EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
		entityManager.persist(romain);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		
		entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Car> createQuery = cb.createQuery(Car.class);
		Root<Car> cars = createQuery.from(Car.class);
		TypedQuery<Car> selectAllCars = entityManager.createQuery(createQuery.select(cars));
		entityManager.close();
		assertEquals("The car of Romain has not been persisted",1,selectAllCars.getResultList().size());
		
	}

}
