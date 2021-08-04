package com.common.service.impl;

import com.common.service.BaseCriteriaService;
import com.common.specification.SearchCriteria;
import com.google.common.reflect.TypeToken;
import com.util.ConvertTypeToken;
import com.util.SearchCriteriaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 11:20
 */
@Service
public abstract class BaseCriteriaServiceImpl<T> implements BaseCriteriaService {

    @Autowired
    private EntityManager entityManager;

    private final ConvertTypeToken<T, T> convertTypeToken = new ConvertTypeToken<>();

    @Override
    public <R> List<R> findAllToSelectionFields(SearchCriteria searchCriteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Type type = new TypeToken<T>(getClass()) {
        }.getType();
        Root<T> root = cq.from(convertTypeToken.convertClassForEntity(type));
        Map<String, From> joinMap = SearchCriteriaUtil.join(searchCriteria.getAliasesSet(), root);

        List<Selection<?>> allSelections = new ArrayList<>();
        List<Selection<?>> selections = SearchCriteriaUtil.select(searchCriteria.getFieldsMap(), joinMap);
        List<Selection<?>> functionFieldSelections = SearchCriteriaUtil
                .functionSelectFields(searchCriteria.getFunctionFiltersList(), joinMap, cb);
        allSelections.addAll(selections);
        allSelections.addAll(functionFieldSelections);
        cq.multiselect(allSelections);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesAnd = SearchCriteriaUtil.and(searchCriteria.getFiltersMap(), joinMap, cb);
        List<Predicate> predicatesOr = SearchCriteriaUtil.or(searchCriteria.getOrsMap(), joinMap, cb);
        predicates.addAll(predicatesOr);
        predicates.addAll(predicatesAnd);
        cq.where(predicates.toArray(new Predicate[0]));

        List<Predicate> predicatesHaving = new ArrayList<>();
        List<Predicate> predicatesHavingAnd = SearchCriteriaUtil.and(searchCriteria.getHavingFiltersMap(), joinMap, cb);
        List<Predicate> predicatesHavingOr = SearchCriteriaUtil.or(searchCriteria.getHavingOrsMap(), joinMap, cb);
        predicatesHaving.addAll(predicatesHavingAnd);
        predicatesHaving.addAll(predicatesHavingOr);

        List<Expression<?>> groupBy = SearchCriteriaUtil.groupBy(searchCriteria.getGroupByMap(), joinMap);
        cq.groupBy(groupBy).having(predicatesHaving.toArray(new Predicate[0]));

        cq.orderBy(QueryUtils.toOrders(searchCriteria.getSort(), root, cb));
        TypedQuery<Tuple> tq = entityManager.createQuery(cq);
        Pageable pageable = searchCriteria.getPageable();
        if (pageable != null) {
            tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            tq.setMaxResults(pageable.getPageSize());
        }

        return SearchCriteriaUtil.mapForSelectionFields(tq.getResultList(), searchCriteria.getResultClass(), allSelections);
    }

}
