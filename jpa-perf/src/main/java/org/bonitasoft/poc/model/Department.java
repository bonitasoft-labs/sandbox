package org.bonitasoft.poc.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Department extends VersionEntity {

    private String name;

    @OneToMany
    private Collection<Employee> employees;

    public String getName() {
        return name;
    }

    public void setName(final String deptName) {
        name = deptName;
    }

    public void addEmployee(final Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (employees == null ? 0 : employees.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Department other = (Department) obj;
        if (employees == null) {
            if (other.employees != null) {
                return false;
            }
        } else if (!employees.equals(other.employees)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
