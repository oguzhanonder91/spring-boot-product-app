package com.util;

import com.common.dto.CaboryaRequestDto;
import com.common.specification.BaseSpecificationFilter;
import com.common.specification.CriteriaFuncitonFieldFilter;
import com.common.specification.SearchCriteria;
import org.modelmapper.internal.util.Primitives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 11:05
 */
public final class SearchCriteriaUtil {

    private static final Logger logger = LoggerFactory.getLogger(SearchCriteriaUtil.class);

    private static final String pathSeparator = "\\.";

    private SearchCriteriaUtil() {

    }

    public static Map<String, From> join(Set<String> aliasesSet, Root root) {
        Map<String, From> joinMap = new HashMap<>();
        // joins
        joinMap.put("root", root);
        for (String s : aliasesSet) {
            String[] relations = s.split(pathSeparator);
            if (relations.length > 1) {
                From temp = root;
                for (int i = 1; i < relations.length; i++) {
                    temp = join(temp, relations[i]);
                }
                joinMap.put(s, temp);
            }
        }
        return joinMap;
    }

    public static List<Predicate> and(LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap, Map<String, From> joinMap,
                                      CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, List<BaseSpecificationFilter>> entry : filtersMap.entrySet()) {
            From from = joinMap.get(entry.getKey());
            entry.getValue().forEach(
                    filter -> predicates.add(builder.and(filter.getSearchOperation().predicate(builder, filter.getField(), filter.getValue(), from))));
        }
        return predicates;
    }

    public static List<Predicate> or(LinkedHashMap<String, List<BaseSpecificationFilter>> orsMap, Map<String, From> joinMap,
                                     CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (orsMap.entrySet().size() > 1) {
            Map.Entry<String, List<BaseSpecificationFilter>> entry1 = ((Map.Entry) orsMap.entrySet().toArray()[0]);
            Map.Entry<String, List<BaseSpecificationFilter>> entry2 = ((Map.Entry) orsMap.entrySet().toArray()[1]);
            From from1 = joinMap.get(entry1.getKey());
            From from2 = joinMap.get(entry2.getKey());
            predicates.add(builder.and(
                    builder.or(
                            entry1.getValue().get(0).getSearchOperation().predicate(builder, entry1.getValue().get(0).getField(), entry1.getValue().get(0).getValue(), from1),
                            entry2.getValue().get(0).getSearchOperation().predicate(builder, entry2.getValue().get(0).getField(), entry2.getValue().get(0).getValue(), from2))));
        } else {
            for (Map.Entry<String, List<BaseSpecificationFilter>> entry : orsMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    From from = joinMap.get(entry.getKey());
                    predicates.add(builder.and(
                            builder.or(
                                    entry.getValue().get(0).getSearchOperation().predicate(builder, entry.getValue().get(0).getField(), entry.getValue().get(0).getValue(), from),
                                    entry.getValue().get(1).getSearchOperation().predicate(builder, entry.getValue().get(1).getField(), entry.getValue().get(1).getValue(), from))));
                }
            }
        }
        return predicates;
    }

    public static List<Selection<?>> select(LinkedHashMap<String, String> fieldsMap, Map<String, From> joinMap) {
        List<Selection<?>> selections = new ArrayList();
        for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
            String[] fieldArr = entry.getKey().split(pathSeparator);
            String selectField = fieldArr.length > 1 ? fieldArr[fieldArr.length - 1] : entry.getKey();
            String relation = "root";
            if (fieldArr.length > 1) {
                String search = pathSeparator + selectField;
                relation = "root." + entry.getKey().split(search)[0];
            }
            From from = joinMap.get(relation);
            selections.add(from.get(selectField).alias(entry.getValue()));
        }
        return selections;
    }

    public static List<Selection<?>> functionSelectFields(List<CriteriaFuncitonFieldFilter> functionFiltersList, Map<String, From> joinMap,
                                                          CriteriaBuilder criteriaBuilder) {
        List<Selection<?>> functionalSelectsions = new ArrayList();
        for (CriteriaFuncitonFieldFilter criteriaFuncitonFieldFilter : functionFiltersList) {
            functionalSelectsions.add(criteriaFuncitonFieldFilter.getCriteriaFunctionType()
                    .functionField(criteriaBuilder, criteriaFuncitonFieldFilter.getField(),
                            criteriaFuncitonFieldFilter.getAlias(), joinMap.get(criteriaFuncitonFieldFilter.getRoot()), criteriaFuncitonFieldFilter.getWhens()));
        }
        return functionalSelectsions;
    }


    public static List<Expression<?>> groupBy(LinkedHashMap<String, String> groupByMap, Map<String, From> joinMap) {
        List<Expression<?>> groupByList = new ArrayList();
        for (Map.Entry<String, String> entry : groupByMap.entrySet()) {
            String[] fieldArr = entry.getKey().split(pathSeparator);
            String selectField = fieldArr.length > 1 ? fieldArr[fieldArr.length - 1] : entry.getKey();
            String relation = "root";
            if (fieldArr.length > 1) {
                String search = pathSeparator + selectField;
                relation = "root." + entry.getKey().split(search)[0];
            }
            From from = joinMap.get(relation);
            groupByList.add(from.get(selectField));
        }
        return groupByList;
    }

    private static <R> void rootAndInnerConstructorCreate(List<String> selectFields, Class<R> selectionClass, Map<String, Object> relationMap) {
        /**
         * setResultClass(PermissionDto.class) ve bu dtonun icinde RoleDto var
         * showField("itemId","itemId").showField("id","id").showField("role.code","role.code")
         * bu sekilde bir select sorgusu olustugunda ilk once root olan permissionDto ve
         * inner olan RoleDto nun constructorları oluşturulur. relationMap e atilir.
         */
        try {
            Object re = selectionClass
                    .getDeclaredConstructor()
                    .newInstance();
            relationMap.put("root", re);
            for (String field : selectFields) {
                String last = field.split(pathSeparator)[field.split(pathSeparator).length - 1];
                String firstByLast = field.split(pathSeparator + last)[0];
                if (!relationMap.containsKey(firstByLast) && !"root".equalsIgnoreCase(firstByLast)) {
                    String innerLast = firstByLast.split(pathSeparator)[firstByLast.split(pathSeparator).length - 1];
                    String innerFirstByLast = firstByLast.split(pathSeparator + innerLast)[0];
                    Object rel = relationMap.get(innerFirstByLast);
                    rel = controlObject(rel);
                    Field fieldRel = rel.getClass()
                            .getDeclaredField(innerLast);
                    Object newRel = controlCollection(fieldRel);
                    relationMap.put(firstByLast, newRel);
                }

            }

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            logger.error(e.getLocalizedMessage());
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private static void settingFieldsToConstructors(List<String> selectFields, Map<String, Object> relationMap, Tuple o,
                                                    List<Selection<?>> selections) {
        /**
         * setResultClass(PermissionDto.class) ve bu dtonun icinde RoleDto var
         * showField("itemId","itemId").showField("id","id").showField("role.code","role.code")
         * itemId, id, code(role '  den ) fieldlarının ilgili constructorlarina
         * setlenmesini saglanır.
         */
        for (int i = 0; i < selectFields.size(); i++) {

            String[] key = selectFields.get(i).split(pathSeparator);
            String innerField = key[key.length - 1];
            String search = selectFields.get(i).split(pathSeparator + innerField)[0];

            Object rel = relationMap.get(search);
            try {
                rel = controlObject(rel);
                Field classField = null;
                try {
                    classField = rel.getClass()
                            .getDeclaredField(innerField);
                } catch (NoSuchFieldException e) {
                    logger.error(e.getLocalizedMessage());
                    logger.debug(e.getLocalizedMessage(), e);
                    classField = rel.getClass()
                            .getSuperclass()
                            .getDeclaredField(innerField);
                }
                classField.setAccessible(true);
                classField.set(rel, o.get(selections.get(i).alias(innerField)));

            } catch (SecurityException | IllegalAccessException | NoSuchFieldException ex) {
                logger.error(ex.getLocalizedMessage());
                logger.debug(ex.getLocalizedMessage(), ex);
            }

        }
    }

    private static void settingInnerDtosToRoot(Map<String, Object> relationMap) {
        /**
         * setResultClass(PermissionDto.class) ve bu dtonun icinde RoleDto var
         * showField("itemId","itemId").showField("id","id").showField("role.code","role.code")
         * olusturulan inner dtonun roota setlenmesi
         */
        try {
            for (Map.Entry<String, Object> map : relationMap.entrySet()) {
                String[] key = map.getKey().split(pathSeparator);
                for (int i = key.length - 1; i > -1; i--) {
                    if (!"root".equals(key[i])) {
                        String search = map.getKey().split(pathSeparator + key[i])[0];

                        Object rel = relationMap.get(search);
                        rel = controlObject(rel);
                        Field classField = rel
                                .getClass()
                                .getDeclaredField(key[i]);

                        Object setter = relationMap.get(search + "." + key[i]);

                        classField.setAccessible(true);
                        classField.set(rel, setter);
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | NoSuchFieldException ex) {
            logger.error(ex.getLocalizedMessage());
            logger.debug(ex.getLocalizedMessage(), ex);
        }
    }

    public static <R> List<R> mapForSelectionFields(List<Tuple> resultList, Class<R> selectionClass, List<Selection<?>> selections) {
        List<R> result = new ArrayList();
        List<String> selectFields = selections.stream().map(item -> "root." + item.getAlias()).collect(Collectors.toList());
        for (Tuple o : resultList) {
            Object re = primitiveCheck(selectionClass, o.get(0).toString());
            if (re != null) {
                result.add((R) re);
            } else {
                Map<String, Object> relationMap = new HashMap();

                rootAndInnerConstructorCreate(selectFields, selectionClass, relationMap);

                settingFieldsToConstructors(selectFields, relationMap, o, selections);

                settingInnerDtosToRoot(relationMap);

                result.add((R) relationMap.get("root"));
            }
        }
        return result;
    }

    private static From join(From from, String s) {
        return from.join(s);
    }

    private static Object controlCollection(Field field) {
        Object newObj = null;
        Class<?> clazz;
        Object arrObj;
        String className;
        if (field.toGenericString().split("<").length == 1) {
            className = "";
        } else {
            className = field.toGenericString().split("<")[1].split(">")[0];
        }


        try {
            if (field.getType().isNestmateOf(Collection.class)
                    || field.getType().isNestmateOf(List.class)
                    || field.getType().isNestmateOf(ArrayList.class)) {
                clazz = Class.forName(className);
                arrObj = clazz.getDeclaredConstructor().newInstance();
                newObj = new ArrayList<>(Arrays.asList(arrObj));
            } else if (field.getType().isNestmateOf(Set.class)
                    || field.getType().isNestmateOf(HashSet.class)) {
                clazz = Class.forName(className);
                arrObj = clazz.getDeclaredConstructor().newInstance();
                newObj = new HashSet<>(Arrays.asList(arrObj));
            } else {
                newObj = field.getType()
                        .getDeclaredConstructor()
                        .newInstance();
            }
        } catch (ClassNotFoundException |
                NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return newObj;
    }

    private static Object controlObject(Object rel) {
        if (rel instanceof ArrayList) {
            return ((ArrayList<?>) rel).get(0);
        } else if (rel instanceof HashSet) {
            return ((HashSet<?>) rel).toArray()[0];
        }
        return rel;
    }

    public static Object primitiveCheck(Class selectionClass, String tupleString) {
        Object re = null;
        if (Primitives.isPrimitiveWrapper(selectionClass) || selectionClass.isPrimitive() ||
                selectionClass.equals(String.class) || selectionClass.equals(BigDecimal.class)) {
            Class clazz = null;
            try {
                if (selectionClass.equals(String.class) || selectionClass.equals(BigDecimal.class)) {
                    clazz = selectionClass;
                } else {
                    clazz = Primitives.wrapperFor(selectionClass);
                }
                re = clazz.getConstructor(String.class).newInstance(tupleString);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | SecurityException | NoSuchMethodException e) {
                logger.error(e.getLocalizedMessage());
                logger.debug(e.getLocalizedMessage(), e);
            }
        }
        return re;
    }

    public static SearchCriteria prepareSearchCriteria(CaboryaRequestDto caboryaRequestDto, Class... clazz) {
        SearchCriteria.Builder builder = new SearchCriteria.Builder();

        assert caboryaRequestDto.getFilterDtos() != null;
        builder.and(caboryaRequestDto.getFilterDtos());

        assert caboryaRequestDto.getSortDtos() != null;
        builder.sort(caboryaRequestDto.getSortDtos());

        assert caboryaRequestDto.getPageableDto() != null;
        builder.pageable(caboryaRequestDto.getPageableDto().getPage(), caboryaRequestDto.getPageableDto().getSize());

        assert clazz != null && clazz.length > 0;
        builder.setResultClass(clazz[0]);

        SearchCriteria searchCriteria = builder.build();
        return searchCriteria;
    }

}