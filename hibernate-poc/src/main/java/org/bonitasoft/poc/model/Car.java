/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.poc.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author Romain Bioteau
 * @author Emmanuel Duchastenier
 */
@Entity
@Table(name = "Car")
@Cacheable
public class Car {

    @Version
    private long version;

    @Id
    private String registrationNumber;

    @Column(nullable = false)
    private String constructor;

    private String model;

    private int numberOfDoors;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getConstructor() {
        return constructor;
    }

    public void setConstructor(final String constructor) {
        this.constructor = constructor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(final int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (constructor == null ? 0 : constructor.hashCode());
        result = prime * result + (model == null ? 0 : model.hashCode());
        result = prime * result + numberOfDoors;
        result = prime * result + (registrationNumber == null ? 0 : registrationNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (constructor == null) {
            if (other.constructor != null) {
                return false;
            }
        } else if (!constructor.equals(other.constructor)) {
            return false;
        }
        if (model == null) {
            if (other.model != null) {
                return false;
            }
        } else if (!model.equals(other.model)) {
            return false;
        }
        if (numberOfDoors != other.numberOfDoors) {
            return false;
        }
        if (registrationNumber == null) {
            if (other.registrationNumber != null) {
                return false;
            }
        } else if (!registrationNumber.equals(other.registrationNumber)) {
            return false;
        }
        return true;
    }

}
