package org.bonitasoft.poc.model;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

/**
 * A representation of the model object '<em><b>Address</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Embeddable()
public class Address {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String street = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String city = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String zipCode = null;

	/**
	 * Returns the value of '<em><b>street</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>street</b></em>' feature
	 * @generated
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the '{@link Address#getStreet() <em>street</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newStreet
	 *            the new value of the '{@link Address#getStreet() street}'
	 *            feature.
	 * @generated
	 */
	public void setStreet(String newStreet) {
		street = newStreet;
	}

	/**
	 * Returns the value of '<em><b>city</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>city</b></em>' feature
	 * @generated
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the '{@link Address#getCity() <em>city</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newCity
	 *            the new value of the '{@link Address#getCity() city}' feature.
	 * @generated
	 */
	public void setCity(String newCity) {
		city = newCity;
	}

	/**
	 * Returns the value of '<em><b>zipCode</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>zipCode</b></em>' feature
	 * @generated
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the '{@link Address#getZipCode() <em>zipCode</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newZipCode
	 *            the new value of the '{@link Address#getZipCode() zipCode}'
	 *            feature.
	 * @generated
	 */
	public void setZipCode(String newZipCode) {
		zipCode = newZipCode;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Address " + " [street: " + getStreet() + "]" + " [city: "
				+ getCity() + "]" + " [zipCode: " + getZipCode() + "]";
	}
}
