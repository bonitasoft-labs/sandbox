package org.bonitasoft.poc.lazy.repository;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.lazy.Employee;
import org.bonitasoft.poc.lazy.impl.EmployeeImpl;


public class EmployeeRepository {

    private EntityManager entityManager;

    public EmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Employee save(Employee employee) {
        Employee merge = entityManager.merge(employee);
        entityManager.detach(merge);
        return merge;
    }

    public Employee get(Long id) {
        return entityManager.find(EmployeeImpl.class, id);
    }

    public void remove(Employee employee) {
        entityManager.remove(employee);
    }

}
