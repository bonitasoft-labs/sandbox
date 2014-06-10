package org.bonitasoft.poc.lazy.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bonitasoft.poc.lazy.Address;

@Entity
public class AddressImpl implements Address {

    @Id
    @GeneratedValue
    private long id;
    private String city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
