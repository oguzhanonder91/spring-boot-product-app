package com.common.specification;

import com.util.enums.EntityState;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
    private boolean distinct;
    private Class resultClass;

    private static final String pathSeparator =  "\\.";

    private static final String root = "root";

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

    public static class Builder {
        private final List<Sort.Order> orders = new ArrayList<>();
        private Pageable pageable;
        private final Set<String> aliasesMap = new HashSet<>();
        private final LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap = new LinkedHashMap<>();
        private final LinkedHashMap<String, List<BaseSpecificationFilter>> orsMap = new LinkedHashMap<>();
        private LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        private boolean distinct;
        private Class resultClass;

        public Builder() {
            aliasesMap.add(root);
        }

        public Builder addFilter(SearchOperation searchOperation, String field, Object value) {
            String[] fieldArr = field.split(pathSeparator);
            String relation= root;
            String filterField = fieldArr.length > 1 ? fieldArr[fieldArr.length - 1] : field;

            // adding join
            if (fieldArr.length > 1) {
                String search = pathSeparator + filterField;
                relation = root + "." + field.split(search)[0];
                aliasesMap.add(relation);
            }
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

        public Builder addFilter(SearchOperation searchOperation, String field, Object from, Object to) {
            return addFilter(searchOperation, field, new SearchBetween(from, to));
        }

        public Builder addFilter(SearchOperation searchOperation, String field) {
            return addFilter(searchOperation, field, null);
        }

        public Builder or(SearchOperation searchOperation1, String field1, Object value1,SearchOperation searchOperation2, String field2, Object value2) {
            or(searchOperation1,field1,value1);
            or(searchOperation2,field2,value2);
            return this;
        }

        private Builder or(SearchOperation searchOperation, String field, Object value) {
            String[] fieldArr = field.split(pathSeparator);
            String relation= root;
            String filterField = fieldArr.length > 1 ? fieldArr[fieldArr.length - 1] : field;

            // adding join
            if (fieldArr.length > 1) {
                String search = pathSeparator + filterField;
                relation = root + "." + field.split(search)[0];
                aliasesMap.add(relation);
            }
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
                String[] fieldArr = entry.getKey().split(pathSeparator);
                if (fieldArr.length > 1) {
                    String search = pathSeparator + fieldArr[fieldArr.length - 1];
                    String relation = root + "." + entry.getKey().split(search)[0];
                    aliasesMap.add(relation);
                }
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
            String[] fieldArr = field.split(pathSeparator);
            String relation = root;
            if (fieldArr.length > 1) {
                String search = pathSeparator + fieldArr[fieldArr.length - 1];
                relation += "." + field.split(search)[0];
                aliasesMap.add(relation);
            }
            this.fields.put(field, field);
            return this;
        }

        public Builder showField(String field, String value) {
            String[] fieldArr = field.split(pathSeparator);
            String relation = root;
            if (fieldArr.length > 1) {
                String search = pathSeparator + fieldArr[fieldArr.length - 1];
                relation += "." + field.split(search)[0];
                aliasesMap.add(relation);
            }
            this.fields.put(field, value);
            return this;
        }

        public Builder distinct() {
            this.distinct = true;
            return this;
        }

        public Builder sort(Sort.Direction type, String field) {
            Sort.Order order = null;
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
