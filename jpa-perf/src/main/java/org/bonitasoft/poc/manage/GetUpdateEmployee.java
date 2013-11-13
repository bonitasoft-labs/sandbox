package org.bonitasoft.poc.manage;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bonitasoft.poc.model.Employee;

import com.codahale.metrics.MetricRegistry;

public class GetUpdateEmployee extends UpdateThread {

	public GetUpdateEmployee(final EntityManagerFactory entityManagerFactory, final MetricRegistry metricRegistry) {
		super(entityManagerFactory,metricRegistry);
	}

	@Override
	public void execute(final EntityManager entityManager) {
		final Employee employee = findRandomEmployee(entityManager);
		if(employee != null){
			employee.setTitle(UUID.randomUUID().toString());
		}
	}

}
