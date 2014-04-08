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
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
    public List<Class<?>> getEntityTypesToCleanAfterTest() {
        return Arrays.asList(Garage.class, Person.class, Car.class);
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

        final Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
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
        Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        foundCar.setConstructor("Mercedes-Benz");
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals("Update failed", "Mercedes-Benz", foundCar.getConstructor());
    }

    @Test
    public void updateADetachedCar() throws Exception {
        String origConstructorValue = "Mercedes";
        String newConstructorValue = "Mercedes-Benz";

        final Car myCar = new Car();
        myCar.setRegistrationNumber("45DV38");
        myCar.setConstructor(origConstructorValue);
        myCar.setModel("SLR-500");
        myCar.setNumberOfDoors(3);

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        myCar.setConstructor(newConstructorValue);
        entityManager.merge(myCar);

        // Let's check another thread does not see the update before it is commited by the current thread:
        FindEntityById<Car> myRunnable = new FindEntityById<Car>(Car.class, myCar.getRegistrationNumber());
        Thread thread = new Thread(myRunnable);
        thread.start();
        thread.join();
        assertEquals(origConstructorValue, myRunnable.getEntity().getConstructor());

        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        // another thread is now aware of the new car constructor value:
        myRunnable = new FindEntityById<Car>(Car.class, myCar.getRegistrationNumber());
        Thread thread2 = new Thread(myRunnable);
        thread2.start();
        thread2.join();
        assertEquals(newConstructorValue, myRunnable.getEntity().getConstructor());

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull("Car not found", foundCar);
        assertEquals("Update failed", newConstructorValue, foundCar.getConstructor());
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
        Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        entityManager.remove(foundCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
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

    @Test(expected = IllegalArgumentException.class)
    public void composition_delete_scenation_with_garage_and_cars() {
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Garage garage = new Garage();
        garage.setName("Michaud");
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
            garage.addToCars(myCar);

        }
        garage = entityManager.merge(garage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        TypedQuery<Car> namedQuery = entityManager.createNamedQuery("getCarByConstructor", Car.class);
        namedQuery.setParameter("constructor", "Mercedes");
        List<Car> resultList = namedQuery.getResultList();

        assertEquals("Invalid number of Mercedes", 4, resultList.size());
        Car car = resultList.get(0);
        entityManager.remove(car);
        entityManager.merge(garage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

    @Test
    public void composition_modify_scenation_with_garage_and_cars() {
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Garage garage = new Garage();
        garage.setName("Michaud");
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
            garage.addToCars(myCar);

        }
        garage = entityManager.merge(garage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        TypedQuery<Car> namedQuery = entityManager.createNamedQuery("getCarByConstructor", Car.class);
        namedQuery.setParameter("constructor", "Mercedes");
        List<Car> resultList = namedQuery.getResultList();

        assertEquals("Invalid number of Mercedes", 4, resultList.size());
        Car car = resultList.get(0);
        String registrationNumber = car.getRegistrationNumber();
        car.setNumberOfDoors(35);
        entityManager.merge(car);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        TypedQuery<Garage> garageQuery = entityManager.createNamedQuery("getGarageByName", Garage.class);
        garageQuery.setParameter("name", "Michaud");
        Garage g = garageQuery.getSingleResult();
        Car modifiedCar = null;
        for (Car c : g.getCars()) {
            if (c.getRegistrationNumber().equals(registrationNumber)) {
                modifiedCar = c;
            }
        }
        assertNotNull(modifiedCar);
        assertEquals(35, modifiedCar.getNumberOfDoors());

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
        List<Car> resultList = getAllEntities(Car.class, entityManager);
        entityManager.close();
        assertEquals("The car of Romain has not been persisted", 1, resultList.size());
    }

    @Test
    public void aGarageContainsMultipleCars() {
        final Car car1 = createACar("456ER45", "Ferrari", "Testarossa", 3);
        final Car car2 = createACar("456BJR35", "Ferrari", "Enzo", 3);
        final Car car3 = createACar("45RA36", "McLaren", "F1", 2);

        final Garage myGarage = new Garage();
        myGarage.setName("Chez Jojo");
        myGarage.addToCars(car1);
        myGarage.addToCars(car2);
        myGarage.addToCars(car3);

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myGarage);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        List<Car> resultList = getAllEntities(Car.class, entityManager);
        entityManager.close();
        assertEquals("The car of Romain has not been persisted", 3, resultList.size());
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
        List<Person> resultList = getAllEntities(Person.class, entityManager);
        Person p = resultList.get(0);
        p.setCar(null);
        final Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        entityManager.remove(foundCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        List<Person> myPersons = getAllEntities(Person.class, entityManager);
        p = myPersons.get(0);
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
        List<Person> myPersons = getAllEntities(Person.class, entityManager);
        final Person p = myPersons.get(0);
        final String myCarRegistration = p.getCar().getRegistrationNumber();
        entityManager.remove(p);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNotNull(foundCar);
    }

    @Test
    public void getTheGaragesWhichContainsAtLeastACarOfAType() {
        final Garage joeGarage = new Garage();
        joeGarage.setName("Joe's");
        joeGarage.addToCars(createACar("456ER45", "Ferrari", "Testarossa", 3));
        joeGarage.addToCars(createACar("456BJR35", "Ferrari", "Enzo", 3));
        joeGarage.addToCars(createACar("45RA36", "McLaren", "F1", 0));

        final Garage mikeGarage = new Garage();
        mikeGarage.setName("Mike's");
        mikeGarage.addToCars(createACar("45-RDS-78", "Lamborghini", "Countach", 3));
        mikeGarage.addToCars(createACar("65-PRE-51", "Lotus", "Elan", 3));
        mikeGarage.addToCars(createACar("78-NRD-15", "Ferrari", "Testarossa", 3));

        final Garage johnGarage = new Garage();
        johnGarage.setName("John's");
        johnGarage.addToCars(createACar("45-RBS-74", "Porsche", "911", 3));
        johnGarage.addToCars(createACar("65-NRE-51", "Porsche", "Panamera", 5));
        johnGarage.addToCars(createACar("78-QSD-15", "Porsche", "918", 2));

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
        joeGarage.addToCars(createACar("456ER45", "Ferrari", "Testarossa", 3));
        joeGarage.addToCars(createACar("456BJR35", "Ferrari", "Enzo", 3));
        joeGarage.addToCars(createACar("45RA36", "McLaren", "F1", 0));

        final Garage mikeGarage = new Garage();
        mikeGarage.setName("Mike's");
        mikeGarage.addToCars(createACar("45-RDS-78", "Lamborghini", "Countach", 3));
        mikeGarage.addToCars(createACar("65-PRE-51", "Lotus", "Elan", 3));
        mikeGarage.addToCars(createACar("78-NRD-15", "Ferrari", "Testarossa", 3));

        final Garage johnGarage = new Garage();
        johnGarage.setName("John's");
        johnGarage.addToCars(createACar("45-RBS-74", "Porsche", "911", 3));
        johnGarage.addToCars(createACar("65-NRE-51", "Porsche", "Panamera", 5));
        johnGarage.addToCars(createACar("78-QSD-15", "Porsche", "918", 2));

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

    @Test
    public void accessCarFromL2Cache() throws InterruptedException {
        final Car myCar = buildRandomCar();

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        getCarsByModel(entityManager, myCar.getModel()).get(0);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        System.err
                .println("=== With L2 cache activated and hibernate.show_sql==true, you should'nt see any Hibernate query looged between this log message...");
        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        System.err.println("=== ... and that one.");
    }

    private Car getCarById(final EntityManager entityManager, final String registrationNumber) {
        return entityManager.find(Car.class, registrationNumber);
    }

    private List<Car> getCarsByModel(final EntityManager entityManager, final String model) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Car> criteria = cb.createQuery(Car.class);
        final Root<Car> cars = criteria.from(Car.class);
        criteria.where(cb.equal(cars.get("model"), model));
        final TypedQuery<Car> constructorQuery = entityManager.createQuery(criteria);
        constructorQuery.setFirstResult(0);
        constructorQuery.setMaxResults(20);
        return constructorQuery.getResultList();
    }

    @Test
    public void cleanCacheAfterDeletingACar() {
        final Car myCar = buildRandomCar();

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Query deleteQuery = entityManager.createNativeQuery("DELETE FROM Car WHERE registrationNumber = '" + myCar.getRegistrationNumber() + "'");
        deleteQuery.executeUpdate();
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNull("No Car should have been found in cache", foundCar);
    }

    @Test
    public void cleanCacheAfterDeletingACarInOneTransaction() {
        final Car myCar = buildRandomCar();

        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(myCar);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        Query deleteQuery = entityManager.createQuery("DELETE FROM Car WHERE registrationNumber = '" + myCar.getRegistrationNumber() + "'");
        deleteQuery.executeUpdate();
        final Car foundCar = getCarById(entityManager, myCar.getRegistrationNumber());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
        assertNull("Car has been found in cache", foundCar);
    }

    public void deletingAGarageAlsoDeletesAllItsCars() {
        final Garage garage = new Garage();
        garage.setName("Marcel");
        garage.addToCars(createACar("456ER45", "Ferrari", "Testarossa", 3));
        garage.addToCars(createACar("456BJR35", "Ferrari", "Enzo", 3));

        final PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(garage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final Garage foundGarage = entityManager.find(Garage.class, garage.getId());
        assertEquals(2, foundGarage.getCars().size());
        entityManager.remove(foundGarage);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        List<Car> resultList = getAllEntities(Car.class, entityManager);
        assertEquals("Deleting a garage should also have deleted all related cars", 0, resultList.size());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

}
