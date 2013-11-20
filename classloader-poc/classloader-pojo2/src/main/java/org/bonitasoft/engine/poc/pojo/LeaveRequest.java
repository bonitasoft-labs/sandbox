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

/**
 * @author Emmanuel Duchastenier
 */
public class LeaveRequest {

    private String id;

    private int numberOfDays;

    private String leaveType;

    public LeaveRequest() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
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
     * @return the leaveType
     */
    public String getLeaveType() {
        return leaveType;
    }

    /**
     * @param leaveType
     *            the leaveType to set
     */
    public void setLeaveType(final String leaveType) {
        this.leaveType = leaveType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((leaveType == null) ? 0 : leaveType.hashCode());
        result = prime * result + numberOfDays;
        return result;
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (leaveType == null) {
            if (other.leaveType != null)
                return false;
        } else if (!leaveType.equals(other.leaveType))
            return false;
        if (numberOfDays != other.numberOfDays)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaveRequest [id=" + id + ", numberOfDays=" + numberOfDays + ", leaveType=" + leaveType + "]";
    }

}
