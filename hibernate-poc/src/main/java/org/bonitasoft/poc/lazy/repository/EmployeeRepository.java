package org.bonitasoft.poc.lazy.repository;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.lazy.LazyEmployee;
import org.bonitasoft.poc.lazy.impl.LazyEmployeeImpl;


public class EmployeeRepository {

    private EntityManager entityManager;

    public EmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public LazyEmployee save(LazyEmployee employee) {
        LazyEmployee merge = entityManager.merge(employee);
        entityManager.detach(merge);
        return merge;
    }

    public LazyEmployee get(Long id) {
        return entityManager.find(LazyEmployeeImpl.class, id);
    }

    public void remove(LazyEmployee employee) {
        entityManager.remove(employee);
    }

}
