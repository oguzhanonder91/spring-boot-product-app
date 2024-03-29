package com.common.specification;

import com.util.SearchCriteriaUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    private final SearchCriteria searchCriteria;

    public GenericSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicatesAnd = SearchCriteriaUtil.and(searchCriteria.getFiltersMap(), SearchCriteriaUtil.join(searchCriteria.getAliasesSet(),root) ,builder);
        List<Predicate> predicatesOr = SearchCriteriaUtil.or(searchCriteria.getOrsMap(), SearchCriteriaUtil.join(searchCriteria.getAliasesSet(),root) ,builder);
        predicatesAnd.addAll(predicatesOr);
        return builder.and(predicatesAnd.toArray(new Predicate[0]));
    }
}
