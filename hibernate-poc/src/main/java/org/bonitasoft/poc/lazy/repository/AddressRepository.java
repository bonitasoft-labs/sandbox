package org.bonitasoft.poc.lazy.repository;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.lazy.Address;


public class AddressRepository {

    private EntityManager entityManager;

    public AddressRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Address save(Address address) {
        return entityManager.merge(address);
    }

    public Address get(Long id) {
        return entityManager.find(Address.class, id);
    }

    public void remove(Address address) {
        entityManager.remove(address);
    }

}
