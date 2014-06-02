package org.bonitasoft.poc.model.aggregation.util;

import org.bonitasoft.poc.model.aggregation.Enfant;
import org.bonitasoft.poc.model.aggregation.Papa;

public class FatherBuilder {

    private final Papa papa = new Papa("aFather");

    public static FatherBuilder aFather() {
        return new FatherBuilder();
    }

    public Papa build() {
        return papa;
    }

    public FatherBuilder withChild(final Enfant enfant) {
        papa.setChild(enfant);
        return this;
    }

    public FatherBuilder withChild(final String childName) {
        papa.setChild(new Enfant(childName));
        return this;
    }

    public FatherBuilder withName(final String name) {
        papa.setName(name);
        return this;
    }
}
