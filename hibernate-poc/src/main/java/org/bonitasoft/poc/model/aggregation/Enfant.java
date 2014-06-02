package org.bonitasoft.poc.model.aggregation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Enfant {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Enfant() {
    }

    public Enfant(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Enfant [id=" + id + ", name=" + name + "]";
    }

}
