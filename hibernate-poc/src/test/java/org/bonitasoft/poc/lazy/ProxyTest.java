package org.bonitasoft.poc.lazy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.bonitasoft.poc.lazy.impl.AddressImpl;
import org.bonitasoft.poc.lazy.impl.LazyEmployeeImpl;
import org.bonitasoft.poc.lazy.repository.AddressRepository;
import org.bonitasoft.poc.lazy.repository.EmployeeRepository;
import org.bonitasoft.poc.model.composition.util.ChildRepository;
import org.bonitasoft.poc.model.composition.util.FatherRepository;
import org.bonitasoft.poc.util.EntityManagerUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;



public class ProxyTest {

    private EntityManager entityManager;
    private EmployeeRepository employeeRepository;
    private AddressRepository addressRepository;

    @Before
    public void setUp() {
        entityManager = EntityManagerUtil.getInstance().createEntityManager();
        employeeRepository = new EmployeeRepository(entityManager);
        addressRepository = new AddressRepository(entityManager);
        beginTransaction();
    }

    @After
    public void tearDown() {
        closeTransaction();
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void closeTransaction() {
        if (entityManager.getTransaction().getRollbackOnly()) {
            entityManager.getTransaction().rollback();
        } else {
            entityManager.getTransaction().commit();
        }
    }
    
    @Test
    @Ignore("TO BE DONE")
    public void should_return_setted_attribute_when_setted_before_first_load() throws Exception {
        LazyEmployee employee = createEmployeeWithAddress("Lyon");
        LazyEmployee proxy = (LazyEmployee) Proxy. newProxyInstance(
                LazyEmployee.class.getClassLoader(), new Class[] { LazyEmployee.class },
                new Handler(employee, entityManager));
        
        proxy.setAddress(buildAdress("Grenoble"));
        
        assertThat(proxy.getAddress().getCity()).isEqualTo("Grenoble");
        
    }
    
    @Test
    public void should_save_a_proxy_object() throws Exception {
        LazyEmployee employee = createEmployeeWithAddress("Lyon");
        LazyEmployee proxy = (LazyEmployee) Proxy. newProxyInstance(
                LazyEmployee.class.getClassLoader(), new Class[] { LazyEmployee.class },
                new Handler(employee, entityManager));
        
        LazyEmployee e = getEmployee(new LazyEmployeeImpl());
        
        
        proxy.setAddress(buildAdress("Grenoble"));
        
        LazyEmployee employee3 = getEmployee(proxy);
        entityManager.merge(employee3);
        
        LazyEmployee employee2 = employeeRepository.get(1L);
        assertThat(employee2.getAddress().getCity()).isEqualTo("Grenoble");
        
    }
    
    private LazyEmployee getEmployee(LazyEmployee e) {
        try {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(e);
            return ((Handler) invocationHandler).getEmployee();
        } catch (IllegalArgumentException e1) {
           return e;
        }
    }
    
    @Test
    public void should_return_setted_attribute_when_setted_after_first_load() throws Exception {
        LazyEmployee employee = createEmployeeWithAddress("Lyon");
        LazyEmployee proxy = (LazyEmployee) Proxy. newProxyInstance(
                LazyEmployee.class.getClassLoader(), new Class[] { LazyEmployee.class },
                new Handler(employee, entityManager));
        
        proxy.getAddress();
        proxy.setAddress(buildAdress("Grenoble"));
        
        assertThat(proxy.getAddress().getCity()).isEqualTo("Grenoble");
        
    }
    
    @Test
    public void should_return_saved_attribute_with_lazy_loading() throws Exception {
        entityManager = spy(entityManager);
        LazyEmployee employee = createEmployeeWithAddress("Lyon");
        
        
        employee = employeeRepository.get(1L);
        entityManager.detach(employee);
        
        LazyEmployee proxy = (LazyEmployee) Proxy. newProxyInstance(
                LazyEmployee.class.getClassLoader(), new Class[] { LazyEmployee.class },
                new Handler(employee, entityManager));
        
        Address address = proxy.getAddress();
        
        verify(entityManager).createQuery(anyString());
        assertThat(address.getCity()).isEqualTo("Lyon");
        
    }

    private LazyEmployee createEmployeeWithAddress(String city) {
        AddressImpl address = new AddressImpl();
        address.setCity(city);
        
        LazyEmployeeImpl emp = new LazyEmployeeImpl();
        emp.setName("Colin");
        emp.setAddress(address);
        
        LazyEmployee employee = employeeRepository.save(emp);
        return employee;
    }

    private Address buildAdress(String city){
        AddressImpl address = new AddressImpl();
        address.setCity(city);
        return address;
    }
}
