package org.bonitasoft.poc.lazy;


public interface Address {

    long getId();
    void setId(long id);
    void setCity(String city);
    String getCity();
}
