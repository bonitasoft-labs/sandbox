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
import org.bonitasoft.poc.model.sortable.SortableQueryBuilder;
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

    private Sortable save(Sortable sortable) {
        return entityManager.merge(sortable);
    }

    private List<Sortable> findSorted(String namedQueryName) {
        Query createNamedQuery = entityManager.createNamedQuery(namedQueryName);
        String queryString = createNamedQuery.unwrap(org.hibernate.Query.class).getQueryString();

        queryString = new SortableQueryBuilder().enhanceQuery(queryString);

        System.out.println("This is the query String :" + queryString);

        Query query = entityManager.createQuery(queryString);

        return query.getResultList();
    }

    private List<String> names(List<Sortable> sortables) {
        return extract(sortables, on(Sortable.class).getName());
    }

    private List<Long> ids(List<Sortable> sortables) {
        return extract(sortables, on(Sortable.class).getId());
    }

    @Test
    public void query_with_no_order_by_should_be_sorted_by_id_asc() throws Exception {
        save(new Sortable("cSortable"), new Sortable("aSortable"), new Sortable("bSortable"));

        List<Sortable> sorted = findSorted(Sortable.Queries.NO_ORDER_BY);

        assertThat(ids(sorted)).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void query_order_by_name_should_be_sorted_by_name_then_by_id_asc() throws Exception {
        save(new Sortable("cSortable"), new Sortable("aSortable"), new Sortable("bSortable"));

        List<Sortable> sorted = findSorted(Sortable.Queries.WITH_ORDER_BY_NAME);

        assertThat(names(sorted)).containsExactly("aSortable", "bSortable", "cSortable");
    }

    @Test
    public void query_order_by_name_desc_should_be_sorted_by_name_then_by_id_asc() throws Exception {
        save(new Sortable("cSortable"), new Sortable("aSortable"), new Sortable("aSortable"), new Sortable("bSortable"));

        List<Sortable> sorted = findSorted(Sortable.Queries.WITH_ORDER_BY_NAME_DESC);

        assertThat(names(sorted)).containsExactly("cSortable", "bSortable", "aSortable", "aSortable");
        assertThat(ids(sorted)).containsExactly(1L, 4L, 2L, 3L);
    }

    @Test
    public void query_with_two_order_by_with_different_sort_order_works() throws Exception {
        Sortable c = save(new Sortable("cSortable"));
        save(c, new Sortable("aSortable"), new Sortable("aSortable"), new Sortable("bSortable"));

        List<Sortable> sorted = findSorted(Sortable.Queries.WITH_TWO_ORDER_BY);

        assertThat(names(sorted)).containsExactly("aSortable", "aSortable", "bSortable", "cSortable");
        assertThat(ids(sorted)).containsExactly(3L, 2L, 4L, 1L);
    }
}
