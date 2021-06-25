package com.common.dto;

/**
 * @author Oğuzhan ÖNDER
 * @date 25.06.2021 - 11:39
 */
public class SortDto {
    /**
     * ASC or DESC
     */
    private String direction;
    private String field;

    public SortDto() {
    }

    public SortDto(String direction, String field) {
        this.direction = direction;
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
