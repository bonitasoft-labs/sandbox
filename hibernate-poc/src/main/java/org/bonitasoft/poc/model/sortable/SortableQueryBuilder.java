package org.bonitasoft.poc.model.sortable;


public class SortableQueryBuilder {
    
    private static final String ORDER_BY = " ORDER BY ";

    private static final String ORDER_FIELD = "id";
    private static final String DEFAULT_ORDER = ORDER_FIELD + " ASC";
    
    public String enhanceQuery(String queryString) {
        if (queryString.contains(ORDER_BY)) {
            String orderByClause = getOrderByClause(queryString);
            if (orderByClause.contains("." + ORDER_FIELD + " ")) {
                return queryString;
            }
            return queryString + ", " + DEFAULT_ORDER;
        }
        return queryString + ORDER_BY + DEFAULT_ORDER;
    }
    
    private String getOrderByClause(String queryString) {
        return queryString.substring(queryString.indexOf(ORDER_BY), queryString.length() - 1);
    }
}
