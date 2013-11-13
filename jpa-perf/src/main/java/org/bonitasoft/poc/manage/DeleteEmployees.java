package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class DeleteEmployees extends DeleteThread {

	public DeleteEmployees(final EntityManagerFactory entityManagerFactory, final Counter deleteErrorCounter, final Timer errorTimer,
			final Counter deleteCounter, final Timer deleteTimer,final Counter nbOptimisticLockError,final Counter employeeNotFoundCounter) {
		super(entityManagerFactory, deleteErrorCounter, errorTimer, deleteCounter, deleteTimer,nbOptimisticLockError,employeeNotFoundCounter);
	}

	@Override
	public void execute(final EntityManager entityManager) {
		final Employee employee = findRandomEmployee(entityManager);
		if(employee != null){
			entityManager.remove(employee);
		}
	}

}
