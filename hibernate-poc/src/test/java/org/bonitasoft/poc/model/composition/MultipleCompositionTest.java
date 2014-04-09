package org.bonitasoft.poc.model.composition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.poc.model.composition.FatherBuilder.aFather;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.bonitasoft.poc.util.EntityManagerUtil;
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
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void closeTransaction() {
        entityManager.getTransaction().commit();
    }

    @Test
    public void when_saving_a_father_children_should_be_saved() throws Exception {
        beginTransaction();

        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Manon", "Marcel");

        closeTransaction();
    }

    @Test(expected = PersistenceException.class)
    public void we_cannot_save_a_child_whithout_a_father() throws Exception {
        beginTransaction();
        childRepository.save(new Child("Manon"));
        closeTransaction();
    }

    @Test
    public void when_deleting_a_father_this_children_should_be_also_deleted() throws Exception {
        beginTransaction();

        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        fatherRepository.remove(father);

        List<Child> children = childRepository.getAll();
        assertThat(children).isEmpty();

        closeTransaction();
    }

    @Test
    public void we_can_remove_a_child_to_his_father() throws Exception {
        beginTransaction();

        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());

        father.getChildren().remove(0);
        fatherRepository.save(father);

        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Marcel");

        closeTransaction();
    }

    @Test
    @Ignore("this is not working - do we want that ?")
    public void deleting_a_child_must_remove_it_from_its_parent() throws Exception {
        beginTransaction();
        Father father = fatherRepository.save(aFather().withChild("Manon").withChild("Marcel").build());
        
        childRepository.remove(father.getChildren().get(0));
        closeTransaction();
        beginTransaction();
        Father fetchedFather = fatherRepository.get(father.getId());
        assertThat(fetchedFather.getChildrenNames()).containsOnly("Marcel");
        
        closeTransaction();
    }
}
