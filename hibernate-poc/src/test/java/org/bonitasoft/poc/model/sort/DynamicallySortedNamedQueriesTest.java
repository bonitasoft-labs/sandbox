package org.bonitasoft.poc.model.sort;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.poc.model.composition.util.FatherBuilder.aFather;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.bonitasoft.poc.model.composition.Child;
import org.bonitasoft.poc.model.composition.Father;
import org.bonitasoft.poc.model.composition.util.ChildRepository;
import org.bonitasoft.poc.model.composition.util.FatherRepository;
import org.bonitasoft.poc.model.sortable.Sortable;
import org.bonitasoft.poc.util.EntityManagerUtil;
import org.hibernate.annotations.Sort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DynamicallySortedNamedQueriesTest {

    private FatherRepository fatherRepository;

    private EntityManager entityManager;

    private ChildRepository childRepository;

    @Before
    public void setUp() {
        entityManager = EntityManagerUtil.getInstance().createEntityManager();
        fatherRepository = new FatherRepository(entityManager);
        childRepository = new ChildRepository(entityManager);
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
    
    private void save(Sortable... sortables) {
        for (Sortable sortable : sortables) {
            entityManager.merge(sortable);
        }
    }
    
    private List<Sortable> findSorted(String namedQueryName) {
        Query createNamedQuery = entityManager.createNamedQuery(namedQueryName);
        String queryString = createNamedQuery.unwrap(org.hibernate.Query.class).getQueryString();
        
        queryString += " ORDER BY name";
        
        System.out.println("This is the query String " + queryString);
        
        Query createQuery = entityManager.createQuery(queryString);
        
        return createQuery.getResultList();
    }
    
    private List<String> names(List<Sortable> sortables) {
        return extract(sortables, on(Sortable.class).getName());
    }
    
    @Test
    public void testName() throws Exception {
        save(new Sortable("cSortable"), new Sortable("aSortable"), new Sortable("bSortable"));
        
        List<Sortable> sorted = findSorted(Sortable.Queries.NO_ORDER_BY);
        
        assertThat(names(sorted)).containsExactly("aSortable", "bSortable", "cSortable");
    }   
}
