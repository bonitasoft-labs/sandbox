/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.poc.pojo;

import java.util.Objects;

/**
 * @author Emmanuel Duchastenier
 */
public class LeaveRequest {

    private String id;

    private int numberOfDays;

    public LeaveRequest() {
    }

    /**
     * @return the numberOfDays
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * @param numberOfDays
     *            the numberOfDays to set
     */
    public void setNumberOfDays(final int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfDays);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeaveRequest other = (LeaveRequest) obj;
        if (id != other.id)
            return false;
        if (numberOfDays != other.numberOfDays)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaveRequest [id=" + id + ", numberOfDays=" + numberOfDays + "]";
    }

}
