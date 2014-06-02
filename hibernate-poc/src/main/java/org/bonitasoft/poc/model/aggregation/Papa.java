package org.bonitasoft.poc.model.aggregation;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Papa {

    @Id
    @GeneratedValue
    private Long id;

    // uni-directional aggregation
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "FATHER_ID")
    private Enfant enfant;

    private String name;

    public Papa() {
    }

    public Papa(final String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the enfant
     */
    public Enfant getChild() {
        return enfant;
    }

    /**
     * @param enfant the enfant to set
     */
    public void setChild(final Enfant enfant) {
        this.enfant = enfant;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("enfant", enfant).toString();
    }

}
