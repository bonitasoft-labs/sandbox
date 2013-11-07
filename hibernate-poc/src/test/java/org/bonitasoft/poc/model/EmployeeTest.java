package org.bonitasoft.poc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;

public class EmployeeTest extends AbstractTest {

    @Override
    public List<Class<?>> getEntityTypes() {
        return Arrays.asList(Employee.class, Project.class);
    }

    @Test
    public void storeEmployeeAndProjectInOrderToAssociateThem() {
        final Project project = new Project();
        project.setName("project-A");

        final Employee employee = new Employee();
        employee.setName("Matti");
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(project);
        entityManager.persist(employee);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        employee.addProject(project);
        entityManager.merge(employee);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        // final TypedQuery<Employee> query = entityManager.createQuery("FROM Employee e WHERE name LIKE 'Mat%'", Employee.class);
        // final List<Employee> employees = query.getResultList();
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        final Root<Employee> root = cq.from(Employee.class);
        cq.where(cb.like(root.<String> get("name"), "Mat%"));
        final TypedQuery<Employee> query = entityManager.createQuery(cq);
        final List<Employee> employees = query.getResultList();

        assertEquals(1, employees.size());
        final Employee matti = employees.get(0);
        assertTrue(!matti.getProjects().isEmpty());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

}
