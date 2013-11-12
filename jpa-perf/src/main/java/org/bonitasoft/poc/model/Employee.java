package org.bonitasoft.poc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Employee extends VersionEntity {

    private String name;

    private String surname;

    private String title;

    private Date created;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> addresses;

    public Employee() {
        addresses = new ArrayList<Address>();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(final Address address) {
        addresses.add(address);
    }

    public void removeAddress(final Address address) {
        addresses.remove(address);
    }

    public void setAddresses(final List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", surname=" + surname + ", title=" + title + ", created=" + created + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (addresses == null ? 0 : addresses.hashCode());
        result = prime * result + (created == null ? 0 : created.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (surname == null ? 0 : surname.hashCode());
        result = prime * result + (title == null ? 0 : title.hashCode());
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
        final Employee other = (Employee) obj;
        if (addresses == null) {
            if (other.addresses != null) {
                return false;
            }
        } else if (!addresses.equals(other.addresses)) {
            return false;
        }
        if (created == null) {
            if (other.created != null) {
                return false;
            }
        } else if (!created.equals(other.created)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (surname == null) {
            if (other.surname != null) {
                return false;
            }
        } else if (!surname.equals(other.surname)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

}
