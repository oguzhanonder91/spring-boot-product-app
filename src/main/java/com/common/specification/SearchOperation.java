package com.common.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Locale;

public enum SearchOperation {
    GREATER_THAN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.greaterThan(from.get(field), CastExpression.cast(value, builder));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(GREATER_THAN, field, value);
        }
    },
    LESS_THAN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.lessThan(from.get(field), CastExpression.cast(value, builder));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(LESS_THAN, field, value);
        }
    },
    GREATER_THAN_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.greaterThanOrEqualTo(from.get(field), CastExpression.cast(value, builder));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(GREATER_THAN_EQUAL, field, value);
        }
    },
    LESS_THAN_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.lessThanOrEqualTo(from.get(field), CastExpression.cast(value, builder));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(LESS_THAN_EQUAL, field, value);
        }
    },
    NOT_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.notEqual(from.get(field), value);
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(NOT_EQUAL, field, value);
        }
    },
    EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.equal(from.get(field), value);
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(EQUAL, field, value);
        }
    },
    MATCH() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.like(builder.lower(from.get(field)), "%" + value.toString().toLowerCase(Locale.ENGLISH) + "%");
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(MATCH, field, value);
        }
    },
    MATCH_END() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.like(builder.lower(from.get(field)), value.toString().toLowerCase(Locale.ENGLISH) + "%");
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(MATCH_END, field, value);
        }
    },
    NOT_LIKE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.notLike(from.get(field), value.toString());
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(NOT_LIKE, field, value);
        }
    },
    IN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return from.get(field).in(value);
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IN, field, value);
        }
    },
    NOT_IN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.not(from.get(field).in(value));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(NOT_IN, field, value);
        }
    },
    IS_NULL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNull(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_NULL, field, null);
        }
    },
    IS_NOT_NULL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNotNull(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_NOT_NULL, field, null);
        }
    },
    IS_EMPTY() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isEmpty(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_EMPTY, field, null);
        }
    },
    IS_NOT_EMPTY() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNotEmpty(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_NOT_EMPTY, field, null);
        }
    },
    IS_TRUE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isTrue(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_TRUE, field, null);
        }
    },
    IS_FALSE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isFalse(from.get(field));
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(IS_FALSE, field, null);
        }
    },
    BETWEEN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            if (value instanceof SearchBetween) {
                SearchBetween searchBetween = (SearchBetween) value;
                return builder.and(builder.greaterThanOrEqualTo(from.get(field), searchBetween.getFrom().toString()),
                        builder.lessThanOrEqualTo(from.get(field), searchBetween.getTo().toString()));
            }
            return builder.between(from.get(field), "", "");
        }

        @Override
        public void selectMethod(SearchCriteria.Builder builder, String field, Object value) {
            builder.and(BETWEEN, field, value);
        }
    };

    public abstract Predicate predicate(CriteriaBuilder builder, String field, Object value, From from);

    public abstract void selectMethod(SearchCriteria.Builder builder, String field, Object value);
}
