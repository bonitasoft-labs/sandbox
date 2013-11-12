package org.bonitasoft.poc.manage;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Address;
import org.bonitasoft.poc.model.Employee;

public class InsertEmployeeThread extends JPAThread {

    public InsertEmployeeThread(final EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public void execute(final EntityManager entityManager) {
        final Employee employee = new Employee();
        employee.setName("Matti" + UUID.randomUUID().toString());
        employee.setCreated(new Date());
        final Random random = new Random();
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            final Address address = new Address();
            address.setStreet(UUID.randomUUID().toString());
            address.setCity(UUID.randomUUID().toString());
            employee.addAddress(address);
        }
        entityManager.persist(employee);
    }

}
