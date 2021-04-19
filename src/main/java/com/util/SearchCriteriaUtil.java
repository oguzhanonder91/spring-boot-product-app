package com.util;

import com.common.specification.BaseSpecificationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<Predicate> where(LinkedHashMap<String, List<BaseSpecificationFilter>> filtersMap, Map<String, From> joinMap,
                                        CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, List<BaseSpecificationFilter>> entry : filtersMap.entrySet()) {
            From from = joinMap.get(entry.getKey());
            entry.getValue().forEach(
                    filter -> predicates.add(filter.getSearchOperation().predicate(builder, filter.getField(), filter.getValue(), from)));
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

    private static <R> void rootAndInnerConstructorCreate(List<String> selectFields, Class<R> selectionClass, Map<String, Object> relationMap) {
        /**
         * setResultClass(PermissionDto.class) ve bu dtonun icinde RoleDto var
         * showField("itemId","itemId").showField("id","id").showField("role.code","role.code")
         * bu sekilde bir select sorgusu olustugunda ilk once root olan permissionDto ve
         * inner olan RoleDto nun constructorları oluşturulur. relationMap e atilir.
         */
        try {
            for (String field : selectFields) {
                List<String> fieldRelations = Arrays.asList(field.split(pathSeparator));
                for (int i = 0; i < fieldRelations.size(); i++) {
                    if (!relationMap.containsKey(fieldRelations.get(i)) && i == 0) {
                        Object re = selectionClass
                                .getDeclaredConstructor()
                                .newInstance();
                        relationMap.put(fieldRelations.get(i), re);
                    }
                    /**
                     * fieldRelations.size() > 2 root.itemId durumunda itemId fieldin constructor
                     * olusmasini engellemek icin (fieldRelations.size() - 1 != i) root.role.code
                     * durumunda son field olan code constructor olusmasini engellemek icin
                     */
                    if (fieldRelations.size() > 2 && fieldRelations.size() - 1 != i) {
                        String tempKey = "root";
                        for (int j = 1; j <= i; j++) {
                            tempKey = tempKey + "." + fieldRelations.get(j);
                        }
                        if (!relationMap.containsKey(tempKey)) {
                            String mapKey = tempKey.split(pathSeparator + fieldRelations.get(i))[0];
                            Object rel = relationMap.get(mapKey);
                            rel = controlObject(rel);
                            Field fieldRel = rel.getClass()
                                    .getDeclaredField(fieldRelations.get(i));

                            Object newRel = controlCollection(fieldRel);
                            String search = field.split(pathSeparator + fieldRelations.get(i))[0];
                            String key = search + "." + fieldRelations.get(i);
                            relationMap.put(key, newRel);
                        }

                    }
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
            Map<String, Object> relationMap = new HashMap();

            rootAndInnerConstructorCreate(selectFields, selectionClass, relationMap);

            settingFieldsToConstructors(selectFields, relationMap, o, selections);

            settingInnerDtosToRoot(relationMap);

            result.add((R) relationMap.get("root"));
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
        String className = field.toGenericString().split("<")[1].split(">")[0];
        try {
            if (field.getType().getName().equals("java.util.Collection")
                    || field.getType().getName().equals("java.util.List")
                    || field.getType().getName().equals("java.util.ArrayList")) {
                clazz = Class.forName(className);
                arrObj = clazz.getDeclaredConstructor().newInstance();
                newObj = new ArrayList<>(Arrays.asList(arrObj));
            } else if (field.getType().getName().equals("java.util.Set")
                    || field.getType().getName().equals("java.util.HashSet")) {
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

}