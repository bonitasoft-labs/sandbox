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
package org.bonitasoft.poc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.bonitasoft.poc.util.PersistenceUtil;
import org.junit.Test;

/**
 * @author Romain Bioteau
 * @author Emmanuel Duchastenier
 * @author Matthieu Chaffotte
 */
public class CarTest extends AbstractTest {

    @Override
    public List<Class<?>> getEntityTypes() {
        return Arrays.asList(Garage.class, Person.class, Car.class);
    }

    @Test(expected = PersistenceException.class)
    public void outdatedModificationThrowsException() throws Exception {
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

    private Car buildRandomCar() {
        final Car myCar = new Car();
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
        final Car myCar = new Car();
        myCar.setRegistrationNumber("316DJV38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        final Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals(foundCar, myCar);
    }

    @Test
    public void updateCar() {
        final Car myCar = new Car();
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
    public void updateADetachedCar() {
        final Car myCar = new Car();
        myCar.setRegistrationNumber("45DV38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar.setConstructor("Mercedes-Benz");
        entityManager.merge(foundCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals("Update failed", "Mercedes-Benz", foundCar.getConstructor());
    }

    @Test
    public void deleteCar() {
        final Car myCar = new Car();
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
            final Car myCar = new Car();
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

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Car> criteria = cb.createQuery(Car.class);
        final Root<Car> cars = criteria.from(Car.class);
        criteria.where(cb.equal(cars.get("constructor"), "Mercedes"));
        final TypedQuery<Car> constructorQuery = entityManager.createQuery(criteria);
        constructorQuery.setFirstResult(0);
        constructorQuery.setMaxResults(20);
        final List<Car> result = constructorQuery.getResultList();
        assertEquals("Invalid number of Mercedes", 4, result.size());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

    @Test(expected = PersistenceException.class)
    public void notNullConstraint() {
        final Car myCar = new Car();
        myCar.setRegistrationNumber("145AA38");
        myCar.setConstructor(null);
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        try {
            entityManager.persist(myCar);
            fail("Car constructor is null, car cannot be persisted");
        } finally {
            entityManager.close();
        }
    }

    @Test
    public void aCarBelongsToAPerson() {
        final Car myCar = new Car();
        myCar.setRegistrationNumber("145AA38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        final Person romain = new Person();
        romain.setFirstName("Romain");
        romain.setLastName("Bioteau");
        romain.setCar(myCar);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(romain);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Car> createQuery = cb.createQuery(Car.class);
        final Root<Car> cars = createQuery.from(Car.class);
        final TypedQuery<Car> selectAllCars = entityManager.createQuery(createQuery.select(cars));
        entityManager.close();
        assertEquals("The car of Romain has not been persisted", 1, selectAllCars.getResultList().size());
    }

    @Test
    public void aGarageContainsMultipleCars() {
        final Car car1 = createACar("456ER45", "Ferrari", "Testarossa", 3);
        final Car car2 = createACar("456BJR35", "Ferrari", "Enzo", 3);
        final Car car3 = createACar("45RA36", "McLaren", "F1", 2);

        final Garage myGarage = new Garage();
        myGarage.setName("Chez Jojo");
        myGarage.addCar(car1);
        myGarage.addCar(car2);
        myGarage.addCar(car3);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myGarage);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Car> createQuery = cb.createQuery(Car.class);
        final Root<Car> cars = createQuery.from(Car.class);
        final TypedQuery<Car> selectAllCars = entityManager.createQuery(createQuery.select(cars));
        entityManager.close();
        assertEquals("The car of Romain has not been persisted", 3, selectAllCars.getResultList().size());
    }

    @Test
    public void aPersonHasAnEmbeddedAdress() {
        final Person romain = new Person();
        romain.setFirstName("Romain");
        romain.setLastName("Bioteau");

        final Address myAddress = new Address();
        myAddress.setStreet("32, rue Gustave Eiffel");
        myAddress.setZipCode("38000");
        myAddress.setCity("Grenoble");
        romain.setAddress(myAddress);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        final EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(romain);
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    private Car createACar(final String registrationNumber, final String brand, final String model, final int numberOfDoors) {
        final Car myCar = new Car();
        myCar.setRegistrationNumber(registrationNumber);
        myCar.setConstructor(brand);
        myCar.setModel(model);
        myCar.setNumberOfDoors(numberOfDoors);
        return myCar;
    }

    @Test
    public void deleteCarBelongingToAPersonWithoutDeletingThePerson() {
        final Car myCar = new Car();
        myCar.setRegistrationNumber("145AA38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        final Person romain = new Person();
        romain.setFirstName("Romain");
        romain.setLastName("Bioteau");
        romain.setCar(myCar);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(romain);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cQuery = cb.createQuery(Person.class);
        Root<Person> persons = cQuery.from(Person.class);
        Person p = entityManager.createQuery(cQuery.select(persons)).getResultList().get(0);
        p.setCar(null);
        final Car foundCar = entityManager.find(Car.class, myCar.getRegistrationNumber());
        entityManager.remove(foundCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        cb = entityManager.getCriteriaBuilder();
        cQuery = cb.createQuery(Person.class);
        persons = cQuery.from(Person.class);
        p = entityManager.createQuery(cQuery.select(persons)).getResultList().get(0);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull(p);
    }

    @Test
    public void deleteAPersonWithACarWithoutDeletingTheCar() {
        final Car myCar = new Car();
        myCar.setRegistrationNumber("145AA38");
        myCar.setConstructor("Mercedes");
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        final Person romain = new Person();
        romain.setFirstName("Romain");
        romain.setLastName("Bioteau");
        romain.setCar(myCar);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(romain);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Person> cQuery = cb.createQuery(Person.class);
        final Root<Person> persons = cQuery.from(Person.class);
        final Person p = entityManager.createQuery(cQuery.select(persons)).getResultList().get(0);
        final String myCarRegistration = p.getCar().getRegistrationNumber();
        entityManager.remove(p);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Car foundCar = entityManager.find(Car.class, myCarRegistration);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull(foundCar);
    }

    @Test
    public void getTheGaragesWhichContainsAtLeastACarOfAType() {
        final Garage joeGarage = new Garage();
        joeGarage.setName("Joe's");
        joeGarage.addCar(createACar("456ER45", "Ferrari", "Testarossa", 3));
        joeGarage.addCar(createACar("456BJR35", "Ferrari", "Enzo", 3));
        joeGarage.addCar(createACar("45RA36", "McLaren", "F1", 0));

        final Garage mikeGarage = new Garage();
        mikeGarage.setName("Mike's");
        mikeGarage.addCar(createACar("45-RDS-78", "Lamborghini", "Countach", 3));
        mikeGarage.addCar(createACar("65-PRE-51", "Lotus", "Elan", 3));
        mikeGarage.addCar(createACar("78-NRD-15", "Ferrari", "Testarossa", 3));

        final Garage johnGarage = new Garage();
        johnGarage.setName("John's");
        johnGarage.addCar(createACar("45-RBS-74", "Porsche", "911", 3));
        johnGarage.addCar(createACar("65-NRE-51", "Porsche", "Panamera", 5));
        johnGarage.addCar(createACar("78-QSD-15", "Porsche", "918", 2));

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(joeGarage);
        entityManager.persist(mikeGarage);
        entityManager.persist(johnGarage);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        // final TypedQuery<Garage> query = entityManager.createQuery("SELECT g FROM Garage g ,IN (g.cars) c WHERE c.model = 'Testarossa'", Garage.class);
        // final TypedQuery<Garage> query = entityManager.createQuery("SELECT g FROM Garage g JOIN FETCH g.cars c WHERE c.model = 'Testarossa'", Garage.class);
        // final List<Garage> garages = query.getResultList();

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Garage> cq = cb.createQuery(Garage.class);
        final Root<Garage> from = cq.from(Garage.class);
        final Join<Garage, Car> join = from.join("cars");
        cq.where(cb.equal(join.get("model"), "Testarossa"));
        final TypedQuery<Garage> select = entityManager.createQuery(cq.select(from));
        final List<Garage> garages = select.getResultList();

        entityManager.close();
        assertEquals("Only two garages contains a Ferrari Testarossa", 2, garages.size());
    }

    @Test
    public void getWallyFromEmbeddedAddress() {
        final Address address = new Address();
        address.setStreet("21 Spring Street");
        address.setCity("Winter town");
        address.setStreet("Summerland");

        final Person wally = new Person();
        wally.setFirstName("Wally");
        wally.setLastName("Doe");
        wally.setAddress(address);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(wally);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        // final TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.address.city = 'Winter town'", Person.class);
        // final List<Person> persons = query.getResultList();

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        final Root<Person> from = cq.from(Person.class);
        cq.where(cb.equal(from.get("address").get("city"), "Winter town"));
        final TypedQuery<Person> select = entityManager.createQuery(cq.select(from));
        final List<Person> persons = select.getResultList();

        entityManager.close();
        assertEquals("Only Wally lives at Winter town", 1, persons.size());
    }

    @Test
    public void getWallyFromNotEmbeddedCar() {
        final Person wally = new Person();
        wally.setFirstName("Wally");
        wally.setLastName("Doe");
        wally.setCar(createACar("456ER45", "Ferrari", "Testarossa", 3));

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(wally);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        // final TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.car.model= 'Testarossa'", Person.class);
        // final List<Person> persons = query.getResultList();

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        final Root<Person> from = cq.from(Person.class);
        cq.where(cb.equal(from.get("car").get("model"), "Testarossa"));
        final TypedQuery<Person> select = entityManager.createQuery(cq.select(from));
        final List<Person> persons = select.getResultList();

        entityManager.close();
        assertEquals("Only Wally lives at Winter town", 1, persons.size());
    }

    @Test
    public void countTheNumberOfGaragesWhichContainsAtLeastACarOfTheConctructor() {
        final Garage joeGarage = new Garage();
        joeGarage.setName("Joe's");
        joeGarage.addCar(createACar("456ER45", "Ferrari", "Testarossa", 3));
        joeGarage.addCar(createACar("456BJR35", "Ferrari", "Enzo", 3));
        joeGarage.addCar(createACar("45RA36", "McLaren", "F1", 0));

        final Garage mikeGarage = new Garage();
        mikeGarage.setName("Mike's");
        mikeGarage.addCar(createACar("45-RDS-78", "Lamborghini", "Countach", 3));
        mikeGarage.addCar(createACar("65-PRE-51", "Lotus", "Elan", 3));
        mikeGarage.addCar(createACar("78-NRD-15", "Ferrari", "Testarossa", 3));

        final Garage johnGarage = new Garage();
        johnGarage.setName("John's");
        johnGarage.addCar(createACar("45-RBS-74", "Porsche", "911", 3));
        johnGarage.addCar(createACar("65-NRE-51", "Porsche", "Panamera", 5));
        johnGarage.addCar(createACar("78-QSD-15", "Porsche", "918", 2));

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(joeGarage);
        entityManager.persist(mikeGarage);
        entityManager.persist(johnGarage);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<Garage> from = cq.from(Garage.class);
        final Join<Object, Object> join = from.join("cars");
        cq.select(cb.countDistinct(from));
        cq.where(cb.equal(join.get("constructor"), "Porsche"));
        final TypedQuery<Long> select = entityManager.createQuery(cq);
        final Long count = select.getSingleResult();
        entityManager.close();
        assertEquals("Only one garage contains cars built by Porsche", Long.valueOf(1), count);
    }

}
