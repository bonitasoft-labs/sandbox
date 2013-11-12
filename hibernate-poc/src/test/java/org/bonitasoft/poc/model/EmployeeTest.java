package org.bonitasoft.poc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;

public class EmployeeTest extends AbstractTest {

    @Override
    public List<Class<?>> getEntityTypesToCleanAfterTest() {
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
        employee.addToProjects(project);
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
        assertFalse(matti.getProjects().isEmpty());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

    @Test
    public void removeAProjectOfAnEmployeeDoesNotRemoveTheEmployee() {
        final Project project = new Project();
        project.setName("project-A");

        final Employee employee = new Employee();
        employee.setName("Matti");
        employee.addToProjects(project);
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(project);
        entityManager.persist(employee);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        TypedQuery<Employee> query = entityManager.createQuery("FROM Employee e WHERE name LIKE 'Mat%'", Employee.class);
        List<Employee> employees = query.getResultList();
        assertEquals(1, employees.size());
        Employee matti = employees.get(0);
        matti.getProjects().clear();
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        query = entityManager.createQuery("FROM Employee e WHERE name LIKE 'Mat%'", Employee.class);
        employees = query.getResultList();
        assertEquals(1, employees.size());
        matti = employees.get(0);
        assertTrue(matti.getProjects().isEmpty());
        persistenceUtil.closeTransactionAndEntityManager(entityManager);
    }

    @Test
    public void cannotRemoveAnProjectAttachedToAnEmployee() {
        final Project project = new Project();
        project.setName("project-A");

        final Employee employee = new Employee();
        employee.setName("Matti");
        employee.addToProjects(project);
        EntityManager entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        entityManager.persist(project);
        entityManager.persist(employee);
        persistenceUtil.closeTransactionAndEntityManager(entityManager);

        entityManager = persistenceUtil.createEntityManagerAndBeginTransaction();
        final TypedQuery<Project> query = entityManager.createQuery("FROM Project p WHERE name IN('project-A')", Project.class);
        final List<Project> projects = query.getResultList();
        assertEquals(1, projects.size());
        final Project projectA = projects.get(0);
        entityManager.remove(projectA);
        try {
            persistenceUtil.closeTransactionAndEntityManager(entityManager);
            fail("Cannot remove a project which is associated to an employee");
        } catch (final RollbackException re) {

        }
    }

}
