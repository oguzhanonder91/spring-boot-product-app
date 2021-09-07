package com.common.specification;

import com.common.dto.FilterDto;
import com.common.dto.SortDto;
import com.util.enums.EntityState;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SearchCriteria {

    private Pageable pageable;
    private Sort sort;
    private Set<String> aliasesSet = new HashSet<>();
    private LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<BaseSpecificationFilter>> orsMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> fieldsMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> groupByMap = new LinkedHashMap<>();
    private List<CriteriaFuncitonFieldFilter> functionFiltersList = new ArrayList<>();
    private LinkedHashMap<String, List<BaseSpecificationFilter>> havingFiltersMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<BaseSpecificationFilter>> havingOrsMap = new LinkedHashMap<>();
    private boolean distinct;
    private Class resultClass;

    private static final String pathSeparator = "\\.";

    private static final String root = "root";

    private static final String COMMA = ",";


    private SearchCriteria() {
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Set<String> getAliasesSet() {
        return aliasesSet;
    }

    public void setAliasesSet(Set<String> aliasesSet) {
        this.aliasesSet = aliasesSet;
    }

    public LinkedHashMap<String, List<BaseSpecificationFilter>> getFiltersMap() {
        return filtersMap;
    }

    public void setFiltersMap(LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap) {
        this.filtersMap = filtersMap;
    }

    public LinkedHashMap<String, String> getFieldsMap() {
        return fieldsMap;
    }

    public void setFieldsMap(LinkedHashMap<String, String> fieldsMap) {
        this.fieldsMap = fieldsMap;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }

    public LinkedHashMap<String, List<BaseSpecificationFilter>> getOrsMap() {
        return orsMap;
    }

    public void setOrsMap(LinkedHashMap<String, List<BaseSpecificationFilter>> orsMap) {
        this.orsMap = orsMap;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public LinkedHashMap<String, String> getGroupByMap() {
        return groupByMap;
    }

    public void setGroupByMap(LinkedHashMap<String, String> groupByMap) {
        this.groupByMap = groupByMap;
    }

    public LinkedHashMap<String, List<BaseSpecificationFilter>> getHavingFiltersMap() {
        return havingFiltersMap;
    }

    public void setHavingFiltersMap(LinkedHashMap<String, List<BaseSpecificationFilter>> havingFiltersMap) {
        this.havingFiltersMap = havingFiltersMap;
    }

    public LinkedHashMap<String, List<BaseSpecificationFilter>> getHavingOrsMap() {
        return havingOrsMap;
    }

    public void setHavingOrsMap(LinkedHashMap<String, List<BaseSpecificationFilter>> havingOrsMap) {
        this.havingOrsMap = havingOrsMap;
    }

    public List<CriteriaFuncitonFieldFilter> getFunctionFiltersList() {
        return functionFiltersList;
    }

    public void setFunctionFiltersList(List<CriteriaFuncitonFieldFilter> functionFiltersList) {
        this.functionFiltersList = functionFiltersList;
    }

    public static class Builder {
        private final List<Sort.Order> orders = new ArrayList<>();
        private Pageable pageable;
        private final Set<String> aliasesMap = new HashSet<>();
        private final LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap = new LinkedHashMap<>();
        private final LinkedHashMap<String, List<BaseSpecificationFilter>> orsMap = new LinkedHashMap<>();
        private LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        private LinkedHashMap<String, String> groupByMap = new LinkedHashMap<>();
        private LinkedHashMap<String, List<BaseSpecificationFilter>> havingFiltersMap = new LinkedHashMap<>();
        private LinkedHashMap<String, List<BaseSpecificationFilter>> havingOrsMap = new LinkedHashMap<>();
        private List<CriteriaFuncitonFieldFilter> functionFiltersList = new ArrayList<>();
        private boolean distinct;
        private Class resultClass;

        public Builder() {
            aliasesMap.add(root);
        }

        public Builder and(SearchOperation searchOperation, String field, Object value) {
            String[] result = addedAliases(field);
            String relation = result[0];
            String filterField = result[1];
            // adding filter
            BaseSpecificationFilter filterDto = new BaseSpecificationFilter(filterField, value, searchOperation);
            if (filtersMap.containsKey(relation)) {
                filtersMap.get(relation).add(filterDto);
            } else {
                List<BaseSpecificationFilter> adaletCoreSpecificationFilters = new ArrayList<>();
                adaletCoreSpecificationFilters.add(filterDto);
                filtersMap.put(relation, adaletCoreSpecificationFilters);
            }
            return this;
        }

        public Builder and(SearchOperation searchOperation, String field, Object from, Object to) {
            return and(searchOperation, field, new SearchBetween(from, to));
        }

        public Builder and(SearchOperation searchOperation, String field) {
            return and(searchOperation, field, null);
        }

        public Builder and(List<FilterDto> filterDtos) {
            if (filterDtos != null && !filterDtos.isEmpty()) {
                for (FilterDto filterDto : filterDtos) {
                    SearchOperation.valueOf(filterDto.getOperation()).selectMethod(this, filterDto.getField(), filterDto.getValue());
                }
            }
            return this;
        }

        public Builder or(SearchOperation searchOperation1, String field1, Object value1, SearchOperation searchOperation2, String field2, Object value2) {
            or(searchOperation1, field1, value1);
            or(searchOperation2, field2, value2);
            return this;
        }

        private Builder or(SearchOperation searchOperation, String field, Object value) {
            String[] result = addedAliases(field);
            String relation = result[0];
            String filterField = result[1];
            // or operation
            BaseSpecificationFilter filterDto = new BaseSpecificationFilter(filterField, value, searchOperation);
            if (orsMap.containsKey(relation)) {
                orsMap.get(relation).add(filterDto);
            } else {
                List<BaseSpecificationFilter> adaletCoreSpecificationFilters = new ArrayList<>();
                adaletCoreSpecificationFilters.add(filterDto);
                orsMap.put(relation, adaletCoreSpecificationFilters);
            }
            return this;
        }

        public Builder showFields(LinkedHashMap<String, String> fields) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                addedAliases(entry.getKey());
            }
            this.fields = fields;
            return this;
        }

        public Builder showFields(Set<String> fields) {
            for (String s : fields) {
                showField(s);
            }
            return this;
        }

        public Builder showField(String field) {
            List<String> fields = Arrays.asList(field.split(COMMA));
            if (fields.size() > 1) {
                for (String item : fields) {
                    showFieldOp(item, item);
                }
            } else {
                showFieldOp(field, field);
            }
            return this;
        }

        public Builder showField(String field, String value) {
            showFieldOp(field, value);
            return this;
        }

        private void showFieldOp(String field, String value) {
            addedAliases(field);
            this.fields.put(field, value);
        }

        public Builder groupBy(String field) {
            List<String> fields = Arrays.asList(field.split(COMMA));
            if (fields.size() > 1) {
                for (String item : fields) {
                    groupByOperation(item);
                }
            } else {
                groupByOperation(field);
            }
            return this;
        }

        private void groupByOperation(String field) {
            addedAliases(field);
            this.groupByMap.put(field, field);
        }

        public Builder havingByAnd(SearchOperation searchOperation, String field, Object value) {
            String[] result = addedAliases(field);
            String relation = result[0];
            String filterField = result[1];
            // adding filter
            BaseSpecificationFilter filterDto = new BaseSpecificationFilter(filterField, value, searchOperation);
            if (havingFiltersMap.containsKey(relation)) {
                havingFiltersMap.get(relation).add(filterDto);
            } else {
                List<BaseSpecificationFilter> adaletCoreSpecificationFilters = new ArrayList<>();
                adaletCoreSpecificationFilters.add(filterDto);
                havingFiltersMap.put(relation, adaletCoreSpecificationFilters);
            }
            return this;
        }

        public Builder havingByAnd(SearchOperation searchOperation, String field, Object from, Object to) {
            return havingByAnd(searchOperation, field, new SearchBetween(from, to));
        }

        public Builder havingByAnd(SearchOperation searchOperation, String field) {
            return havingByAnd(searchOperation, field, null);
        }

        public Builder havingByOr(SearchOperation searchOperation, String field, Object value) {
            String[] result = addedAliases(field);
            String relation = result[0];
            String filterField = result[1];
            // adding or
            BaseSpecificationFilter filterDto = new BaseSpecificationFilter(filterField, value, searchOperation);
            if (havingOrsMap.containsKey(relation)) {
                havingOrsMap.get(relation).add(filterDto);
            } else {
                List<BaseSpecificationFilter> adaletCoreSpecificationFilters = new ArrayList<>();
                adaletCoreSpecificationFilters.add(filterDto);
                havingOrsMap.put(relation, adaletCoreSpecificationFilters);
            }
            return this;
        }

        public Builder havingByOr(SearchOperation searchOperation1, String field1, Object value1,
                                  SearchOperation searchOperation2, String field2, Object value2) {
            havingByOr(searchOperation1, field1, value1);
            havingByOr(searchOperation2, field2, value2);
            return this;
        }

        public Builder function(CriteriaFunctionType criteriaFunctionType, String field, String alias, Object... whens) {
            String[] result = addedAliases(field);
            String relation = result[0];
            String filterField = result[1];
            this.functionFiltersList.add(new CriteriaFuncitonFieldFilter(filterField, relation, alias, criteriaFunctionType, whens));
            return this;
        }

        public Builder distinct() {
            this.distinct = true;
            return this;
        }

        public Builder sort(List<SortDto> sortDtos) {
            if (sortDtos != null && !sortDtos.isEmpty()) {
                for (SortDto sortDto : sortDtos) {
                    sort(Sort.Direction.fromString(sortDto.getDirection()), sortDto.getField());
                }
            }
            return this;
        }

        public Builder sort(Sort.Direction type, String field) {
            Sort.Order order = null;
            addedAliases(field);
            if (type.isAscending()) {
                order = Sort.Order.asc(field);
            }
            if (type.isDescending()) {
                order = Sort.Order.desc(field);
            }
            this.orders.add(order);
            return this;
        }

        public Builder pageable(int page, int pageSize) {
            this.pageable = PageRequest.of(page, pageSize);
            return this;
        }

        public Builder setResultClass(Class clazz) {
            this.resultClass = clazz;
            return this;
        }

        private String[] addedAliases(String field) {
            // 0 : relation , 1: seperatorfield
            String[] strings = new String[2];
            String[] fieldArr = field.split(pathSeparator);
            String relation = root;
            if (fieldArr.length > 1) {
                String separatorField = fieldArr[fieldArr.length - 1];
                String search = pathSeparator + separatorField;
                relation += "." + field.split(search)[0];
                strings[0] = relation;
                strings[1] = separatorField;
                aliasesMap.add(relation);
            } else {
                strings[0] = relation;
                strings[1] = field;
            }
            return strings;
        }

        public SearchCriteria build() {
            SearchCriteria searchCriteria = new SearchCriteria();
            if (this.pageable != null) {
                if (!this.orders.isEmpty()) {
                    searchCriteria
                            .setPageable(PageRequest.of(this.pageable.getPageNumber(), this.pageable.getPageSize(), Sort.by(this.orders)));
                } else {
                    searchCriteria.setPageable(this.pageable);
                }
            }
            searchCriteria.setSort(Sort.by(this.orders));
            searchCriteria.setAliasesSet(this.aliasesMap);
            searchCriteria.setFiltersMap(this.filtersMap);
            searchCriteria.setFieldsMap(this.fields);
            searchCriteria.setOrsMap(this.orsMap);
            searchCriteria.setDistinct(this.distinct);
            searchCriteria.setResultClass(this.resultClass);
            searchCriteria.setGroupByMap(this.groupByMap);
            searchCriteria.setHavingFiltersMap(this.havingFiltersMap);
            searchCriteria.setHavingOrsMap(this.havingOrsMap);
            searchCriteria.setFunctionFiltersList(this.functionFiltersList);
            return searchCriteria;
        }

        public SearchCriteria buildActive() {
            SearchCriteria searchCriteria = this.build();
            searchCriteria.getFiltersMap().forEach((key, value) -> value
                    .add(new BaseSpecificationFilter("entityState", EntityState.ACTIVE, SearchOperation.EQUAL)));
            return searchCriteria;
        }

        public SearchCriteria buildPassive() {
            SearchCriteria searchCriteria = this.build();
            searchCriteria.getFiltersMap().forEach((key, value) -> value
                    .add(new BaseSpecificationFilter("entityState", EntityState.PASSIVE, SearchOperation.EQUAL)));
            return searchCriteria;
        }

    }
}
