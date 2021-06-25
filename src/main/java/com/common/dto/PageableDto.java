package com.common.dto;

/**
 * @author Oğuzhan ÖNDER
 * @date 25.06.2021 - 11:58
 */
public class PageableDto {
    private Integer page;
    private Integer size;

    public PageableDto() {
    }

    public PageableDto(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
