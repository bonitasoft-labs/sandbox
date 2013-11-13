package org.bonitasoft.poc.manage;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class GetUpdateEmployee extends UpdateThread {

	public GetUpdateEmployee(final EntityManagerFactory entityManagerFactory, final Counter updateErrorCounter, final Timer errorTimer,
			final Counter updateCounter, final Timer updatTimer,final Counter nbOptimisticLockError,final Counter employeeNotFoundCounter) {
		super(entityManagerFactory, updateErrorCounter, errorTimer, updateCounter, updatTimer,nbOptimisticLockError,employeeNotFoundCounter);
	}

	@Override
	public void execute(final EntityManager entityManager) {
		final Employee employee = findRandomEmployee(entityManager);
		if(employee != null){
			employee.setTitle(UUID.randomUUID().toString());
		}

	}

}
