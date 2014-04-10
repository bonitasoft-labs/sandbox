package org.bonitasoft.poc.model.composition.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.bonitasoft.poc.model.composition.Child;


public class ChildRepository {

    private EntityManager entityManager;

    public ChildRepository(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public Child save(Child child) {
        return entityManager.merge(child);
    }
    
    public Child get(Long id) {
        return entityManager.find(Child.class, id);
    }

    public List<Child> getAll() {
        return entityManager.createQuery("FROM Child").getResultList();
    }
    
    public void remove(Child child) {
        entityManager.remove(child);
    }
}
