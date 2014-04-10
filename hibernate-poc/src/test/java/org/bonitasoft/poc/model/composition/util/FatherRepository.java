package org.bonitasoft.poc.model.composition.util;

import java.util.List;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.model.composition.Child;
import org.bonitasoft.poc.model.composition.Father;


public class FatherRepository {

    private EntityManager entityManager;

    public FatherRepository(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }
    
    public Father save(Father father) {
        return entityManager.merge(father);
    }
    
    public Father get(Long id) {
        return entityManager.find(Father.class, id);
    }

    public void remove(Father father) {
        entityManager.remove(father);
    }
    
    public List<Child> getAll() {
        return entityManager.createQuery("FROM Father").getResultList();
    }
}
