package org.bonitasoft.poc.manage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.MetricRegistry;

public class DeleteEmployees extends DeleteThread {

	public DeleteEmployees(final EntityManagerFactory entityManagerFactory, final MetricRegistry metricRegistry) {
		super(entityManagerFactory, metricRegistry);
	}

	@Override
	public void execute(final EntityManager entityManager) {
		final Employee employee = findRandomEmployee(entityManager);
		if(employee != null){
			entityManager.remove(employee);
		}
	}

}
