package com.common.dto;

import java.util.List;

/**
 * @author Oğuzhan ÖNDER
 * @date 25.06.2021 - 12:01
 */
public class CaboryaRequestDto {
    private List<SortDto> sortDtos;
    private List<FilterDto> filterDtos;
    private PageableDto pageableDto;

    public CaboryaRequestDto() {
    }

    public CaboryaRequestDto(List<SortDto> sortDtos, List<FilterDto> filterDtos, PageableDto pageableDto) {
        this.sortDtos = sortDtos;
        this.filterDtos = filterDtos;
        this.pageableDto = pageableDto;
    }

    public List<SortDto> getSortDtos() {
        return sortDtos;
    }

    public void setSortDtos(List<SortDto> sortDtos) {
        this.sortDtos = sortDtos;
    }

    public List<FilterDto> getFilterDtos() {
        return filterDtos;
    }

    public void setFilterDtos(List<FilterDto> filterDtos) {
        this.filterDtos = filterDtos;
    }

    public PageableDto getPageableDto() {
        return pageableDto;
    }

    public void setPageableDto(PageableDto pageableDto) {
        this.pageableDto = pageableDto;
    }
}
