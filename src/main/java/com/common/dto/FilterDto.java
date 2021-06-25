package com.common.dto;

/**
 * @author Oğuzhan ÖNDER
 * @date 25.06.2021 - 11:18
 */
public class FilterDto {
    private String field;
    private Object value;
    /**
     * GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL, NOT_EQUAL,
     * EQUAL, MATCH, MATCH_END, NOT_LIKE, IN, NOT_IN, IS_NULL, IS_NOT_NULL,
     * IS_EMPTY, IS_NOT_EMPTY, IS_TRUE, IS_FALSE, BETWEEN
     */
    private String operation;

    public FilterDto() {
    }

    public FilterDto(String field, Object value, String operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
