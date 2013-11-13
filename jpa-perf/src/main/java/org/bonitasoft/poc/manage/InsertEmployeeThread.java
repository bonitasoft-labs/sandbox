package org.bonitasoft.poc.manage;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Address;
import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class InsertEmployeeThread extends InsertThread {

    public InsertEmployeeThread(final EntityManagerFactory entityManagerFactory, final Counter insertionErrorCounter, final Timer errorTimer,
            final Counter insertionCounter, final Timer insertTimer,final Counter optimisticLockErrorCounter) {
        super(entityManagerFactory, insertionErrorCounter, errorTimer, insertionCounter, insertTimer,optimisticLockErrorCounter);
    }

    @Override
    public void execute(final EntityManager entityManager) {
        final Employee employee = new Employee();
        employee.setName("Matti" + UUID.randomUUID().toString());
        Random random = getRandom();
        employee.setAge(random.nextInt(42)+20);
        employee.setCreated(new Date());
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            final Address address = new Address();
            address.setStreet(UUID.randomUUID().toString());
            address.setCity(UUID.randomUUID().toString());
            employee.addAddress(address);
        }
        entityManager.persist(employee);
    }

	

}
