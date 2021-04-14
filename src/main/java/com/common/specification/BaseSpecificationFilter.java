package com.common.specification;

/**
 * @author Oğuzhan ÖNDER
 * @date 23.03.2021 - 12:43
 */

public class BaseSpecificationFilter {
    private String field;
    private Object value;
    private SearchOperation searchOperation;


    public BaseSpecificationFilter() {
    }

    public BaseSpecificationFilter(String field, Object value, SearchOperation searchOperation) {
        this.field = field;
        this.value = value;
        this.searchOperation = searchOperation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SearchOperation getSearchOperation() {
        return searchOperation;
    }

    public void setSearchOperation(SearchOperation searchOperation) {
        this.searchOperation = searchOperation;
    }
}
