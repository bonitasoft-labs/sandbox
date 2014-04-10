package org.bonitasoft.poc.model.sortable;

import static org.apache.commons.lang3.builder.ToStringStyle.SIMPLE_STYLE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@NamedQueries({
        @NamedQuery(name = Sortable.Queries.NO_ORDER_BY, query = "SELECT p FROM Sortable p"),
        @NamedQuery(name = Sortable.Queries.WITH_ORDER_BY_ID, query = "SELECT p FROM Sortable p ORDER BY p.id"),
        @NamedQuery(name = Sortable.Queries.WITH_ORDER_BY_NAME, query = "SELECT p FROM Sortable p ORDER BY p.name"),
        @NamedQuery(name = Sortable.Queries.WITH_ORDER_BY_NAME_DESC, query = "SELECT p FROM Sortable p ORDER BY p.name DESC"),
        @NamedQuery(name = Sortable.Queries.WITH_TWO_ORDER_BY, query = "SELECT p FROM Sortable p ORDER BY p.name ASC, p.id DESC")
})
public class Sortable {

    public interface Queries {
        String NO_ORDER_BY = "sortable.noOrder";
        String WITH_ORDER_BY_NAME = "sortable.alreadyOrderedByName";
        String WITH_ORDER_BY_NAME_DESC = "sortable.alreadyOrderedByNameDesc";
        String WITH_ORDER_BY_ID = "sortable.alreadyOrderedById";
        String WITH_TWO_ORDER_BY= "sortable.twoOrderBy";
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Sortable() {
    }

    public Sortable(String name) {
        super();
        this.name = name;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, SIMPLE_STYLE).append("name", name).toString();
    }
}
