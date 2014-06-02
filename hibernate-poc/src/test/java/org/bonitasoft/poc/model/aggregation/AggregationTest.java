package org.bonitasoft.poc.model.aggregation;

import javax.persistence.EntityManager;

import org.bonitasoft.poc.model.aggregation.util.ChildRepository;
import org.bonitasoft.poc.model.aggregation.util.FatherRepository;
import org.bonitasoft.poc.util.EntityManagerUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static org.bonitasoft.poc.model.aggregation.util.FatherBuilder.aFather;

public class AggregationTest {

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

    @Test
    public void savingAFatherShouldCascadeSaveChildOnAggregation() throws Exception {
        final Papa papa = fatherRepository.save(aFather().withChild("Manon").build());
        final Enfant manon = childRepository.get(1L);

        final Papa fetchedFather = fatherRepository.get(papa.getId());
        assertThat(fetchedFather.getChild().getName()).isEqualTo(manon.getName());
    }

    @Test
    public void deletingAFatherShouldNotCascadeDeleteChildOnAggregation() throws Exception {
        final Papa papa = fatherRepository.save(aFather().withChild("Manon").build());

        fatherRepository.remove(papa);
        final Enfant manon = childRepository.get(1L);
        assertThat(manon).isNotNull();
    }
}
