package org.bonitasoft.poc.manage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Address;
import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class DeleteEmployeesAddress extends DeleteThread {

	public DeleteEmployeesAddress(final EntityManagerFactory entityManagerFactory, final Counter deleteErrorCounter, final Timer errorTimer,
			final Counter deleteCounter, final Timer deleteTimer,final Counter optimisticLockErrorCounter,final Counter employeeNotFoundCounter) {
		super(entityManagerFactory, deleteErrorCounter, errorTimer, deleteCounter, deleteTimer,optimisticLockErrorCounter,employeeNotFoundCounter);
	}

	@Override
	public void execute(final EntityManager entityManager) {
		final Employee employee = findRandomEmployee(entityManager);
		if(employee != null){
			final List<Address> addresses = employee.getAddresses();
			if (!addresses.isEmpty()) {
				final Address address = addresses.get(0);
				employee.removeAddress(address);
			}
		}
	}

}
