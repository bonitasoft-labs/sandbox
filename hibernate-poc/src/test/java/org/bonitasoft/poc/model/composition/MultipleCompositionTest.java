package org.bonitasoft.poc.model.composition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.poc.model.composition.util.FatherBuilder.aFather;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.bonitasoft.poc.model.composition.util.ChildRepository;
import org.bonitasoft.poc.model.composition.util.FatherRepository;
import org.bonitasoft.poc.util.EntityManagerUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MultipleCompositionTest {

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
    public void when_saving_a_father_children_should_be_saved() throws Exception {

        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Manon", "Marcel");
    }

    @Test(expected = PersistenceException.class)
    public void we_cannot_save_a_child_whithout_a_father() throws Exception {
        childRepository.save(new Child("Manon"));
    }

    @Test
    public void when_deleting_a_father_this_children_should_be_also_deleted() throws Exception {
        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        fatherRepository.remove(father);

        assertThat(childRepository.getAll()).isEmpty();
    }

    @Test
    public void we_can_remove_a_child_to_his_father() throws Exception {
        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        father.getChildren().remove(0);
        fatherRepository.save(father);

        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Marcel");
    }

    @Test
    @Ignore("Unbelivable - in fact we can't remove directly a child")
    public void deleting_a_child_must_remove_it_from_its_parent() throws Exception {
        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());
        
        childRepository.remove(father.getChildren().get(0));
        
        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Marcel");
    }
}
