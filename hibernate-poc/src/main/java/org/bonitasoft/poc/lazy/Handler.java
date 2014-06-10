package org.bonitasoft.poc.lazy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

public class Handler implements InvocationHandler {

    private Employee employee;

    // do not need map anymore
    private Map<Method, Object> loaded;
    private EntityManager entityManager;

    public Handler(Employee pojo, EntityManager entityManager) {
        this.employee = pojo;
        this.entityManager = entityManager;
        loaded = new HashMap<Method, Object>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Lazy.class) && !isAlreadyLoaded(method)) {
            Object address = entityManager.createQuery("SELECT e.address FROM EmployeeImpl e WHERE e.id =" + employee.getId()).getSingleResult();
            loaded.put(method, address);
            employee.setAddress((Address) address);
            return address;
        }
        return method.invoke(employee, args);
    }

    private boolean isAlreadyLoaded(Method method) {
        return loaded.containsKey(method);
    }

    public Employee getEmployee() {
        return employee;
    }
}
