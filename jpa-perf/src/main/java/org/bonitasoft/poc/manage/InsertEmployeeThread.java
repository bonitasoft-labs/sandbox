package org.bonitasoft.poc.manage;

import java.util.Date;
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
        final Address address = new Address();
        address.setStreet(UUID.randomUUID().toString());
        address.setCity(UUID.randomUUID().toString());

        final Employee employee = new Employee();
        employee.setName("Matti" + UUID.randomUUID().toString());
        employee.setCreated(new Date());
        employee.addAddress(address);
        entityManager.persist(employee);
    }

}
