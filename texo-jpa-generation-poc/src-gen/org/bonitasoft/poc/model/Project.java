package org.bonitasoft.poc.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * A representation of the model object '<em><b>Project</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "poc_Project")
public class Project {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Id()
	@GeneratedValue()
	private long id = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String name = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinTable()
	private List<Employee> employees = new ArrayList<Employee>();

	/**
	 * Returns the value of '<em><b>id</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>id</b></em>' feature
	 * @generated
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the '{@link Project#getId() <em>id</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newId
	 *            the new value of the '{@link Project#getId() id}' feature.
	 * @generated
	 */
	public void setId(long newId) {
		id = newId;
	}

	/**
	 * Returns the value of '<em><b>name</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>name</b></em>' feature
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the '{@link Project#getName() <em>name</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newName
	 *            the new value of the '{@link Project#getName() name}' feature.
	 * @generated
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Returns the value of '<em><b>employees</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>employees</b></em>' feature
	 * @generated
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * Adds to the <em>employees</em> feature.
	 * 
	 * @param employeesValue
	 *            the value to add
	 * @return true if the value is added to the collection (it was not yet
	 *         present in the collection), false otherwise
	 * @generated
	 */
	public boolean addToEmployees(Employee employeesValue) {
		if (!employees.contains(employeesValue)) {
			employees.add(employeesValue);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes from the <em>employees</em> feature.
	 * 
	 * @param employeesValue
	 *            the value to remove
	 * @return true if the value is removed from the collection (it existed in
	 *         the collection before removing), false otherwise
	 * 
	 * @generated
	 */
	public boolean removeFromEmployees(Employee employeesValue) {
		if (employees.contains(employeesValue)) {
			employees.remove(employeesValue);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears the <em>employees</em> feature.
	 * 
	 * @generated
	 */
	public void clearEmployees() {
		while (!employees.isEmpty()) {
			removeFromEmployees(employees.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Project#getEmployees() <em>employees</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newEmployees
	 *            the new value of the '{@link Project#getEmployees() employees}
	 *            ' feature.
	 * @generated
	 */
	public void setEmployees(List<Employee> newEmployees) {
		employees = newEmployees;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Project " + " [id: " + getId() + "]" + " [name: " + getName()
				+ "]";
	}
}
