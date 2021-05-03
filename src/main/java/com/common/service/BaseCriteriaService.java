package com.common.service;

import com.common.specification.SearchCriteria;

import java.util.List;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 11:00
 */
public interface BaseCriteriaService {

    <R> List<R> findAllToSelectionFields(SearchCriteria searchCriteria);
}
