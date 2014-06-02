package org.bonitasoft.poc.model.composition;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Father {

    @Id
    @GeneratedValue
    private Long id;

    // uni-directional multiple composition
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, name= "father_id")
    private List<Child> children = new ArrayList<Child>();

    // uni-directional nullable single composition
    @OneToOne(orphanRemoval = true)
    private Wife wife;

    private String name;

    public Father() {
    }

    public Father(String name) {
        super();
        this.name = name;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void addChild(Child child) {
        children.add(child);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildrenNames() {
        return extract(children, on(Child.class).getName());
    }

    @Override
    public String toString() {
        return "Father [id=" + id + ", children=" + children + ", wife=" + wife + ", name=" + name + "]";
    }

}
