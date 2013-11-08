package org.bonitasoft.poc.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * A representation of the model object '<em><b>Car</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "Car")
@Table(name = "Car")
public class Car {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Id()
	private String registrationNumber = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Column(nullable = false)
	private String constructor = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String model = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int numberOfDoors = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Version()
	private long version = 0;

	/**
	 * Returns the value of '<em><b>registrationNumber</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>registrationNumber</b></em>' feature
	 * @generated
	 */
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	/**
	 * Sets the '{@link Car#getRegistrationNumber() <em>registrationNumber</em>}
	 * ' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newRegistrationNumber
	 *            the new value of the '{@link Car#getRegistrationNumber()
	 *            registrationNumber}' feature.
	 * @generated
	 */
	public void setRegistrationNumber(String newRegistrationNumber) {
		registrationNumber = newRegistrationNumber;
	}

	/**
	 * Returns the value of '<em><b>constructor</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>constructor</b></em>' feature
	 * @generated
	 */
	public String getConstructor() {
		return constructor;
	}

	/**
	 * Sets the '{@link Car#getConstructor() <em>constructor</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newConstructor
	 *            the new value of the '{@link Car#getConstructor() constructor}
	 *            ' feature.
	 * @generated
	 */
	public void setConstructor(String newConstructor) {
		constructor = newConstructor;
	}

	/**
	 * Returns the value of '<em><b>model</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>model</b></em>' feature
	 * @generated
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the '{@link Car#getModel() <em>model</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newModel
	 *            the new value of the '{@link Car#getModel() model}' feature.
	 * @generated
	 */
	public void setModel(String newModel) {
		model = newModel;
	}

	/**
	 * Returns the value of '<em><b>numberOfDoors</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>numberOfDoors</b></em>' feature
	 * @generated
	 */
	public int getNumberOfDoors() {
		return numberOfDoors;
	}

	/**
	 * Sets the '{@link Car#getNumberOfDoors() <em>numberOfDoors</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newNumberOfDoors
	 *            the new value of the '{@link Car#getNumberOfDoors()
	 *            numberOfDoors}' feature.
	 * @generated
	 */
	public void setNumberOfDoors(int newNumberOfDoors) {
		numberOfDoors = newNumberOfDoors;
	}

	/**
	 * Returns the value of '<em><b>version</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>version</b></em>' feature
	 * @generated
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Sets the '{@link Car#getVersion() <em>version</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newVersion
	 *            the new value of the '{@link Car#getVersion() version}'
	 *            feature.
	 * @generated
	 */
	public void setVersion(long newVersion) {
		version = newVersion;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Car " + " [registrationNumber: " + getRegistrationNumber()
				+ "]" + " [constructor: " + getConstructor() + "]"
				+ " [model: " + getModel() + "]" + " [numberOfDoors: "
				+ getNumberOfDoors() + "]" + " [version: " + getVersion() + "]";
	}
}
