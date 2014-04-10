package org.bonitasoft.poc.model.sortable;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SortableQueryBuilderTest {

    private SortableQueryBuilder queryBuilder;

    @Before
    public void setUp() {
        queryBuilder = new SortableQueryBuilder();
    }

    @Test
    public void should_add_order_if_no_order_specified_in_query() throws Exception {
        String query = "SELECT o FROM TOTO WHERE o.id = :id";
        
        String enhancedQuery = queryBuilder.enhanceQuery(query);
        
        assertThat(enhancedQuery).isEqualTo("SELECT o FROM TOTO WHERE o.id = :id ORDER BY id ASC");
    }
    
    @Test
    public void should_add_order_to_existing_ones() throws Exception {
        String query = "SELECT o FROM TOTO WHERE o.id = :id ORDER BY o.name DESC";
        
        String enhancedQuery = queryBuilder.enhanceQuery(query);
        
        assertThat(enhancedQuery).isEqualTo("SELECT o FROM TOTO WHERE o.id = :id ORDER BY o.name DESC, id ASC");
    }
    
    @Test
    public void should_not_add_order_if_default_order_field_already_an_order_clause_query() throws Exception {
        String query = "SELECT o FROM TOTO WHERE o.id = :id ORDER BY o.name DESC, o.id DESC";
        
        String enhancedQuery = queryBuilder.enhanceQuery(query);
        
        assertThat(enhancedQuery).isEqualTo("SELECT o FROM TOTO WHERE o.id = :id ORDER BY o.name DESC, o.id DESC");
    }
}
