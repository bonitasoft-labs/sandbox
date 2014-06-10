package org.bonitasoft.poc.lazy.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.bonitasoft.poc.lazy.Address;
import org.bonitasoft.poc.lazy.LazyEmployee;

@Entity(name = "LazyEmployee")
public class LazyEmployeeImpl implements LazyEmployee {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    
    @ManyToOne(targetEntity = AddressImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name= "employee_id")
    private Address address;
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
