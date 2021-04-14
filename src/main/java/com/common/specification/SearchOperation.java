package com.common.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Locale;

public enum SearchOperation {
    GREATER_THAN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.greaterThan(from.get(field), value.toString());
        }
    },
    LESS_THAN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.lessThan(from.get(field), value.toString());
        }
    },
    GREATER_THAN_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.greaterThanOrEqualTo(from.get(field), value.toString());
        }
    },
    LESS_THAN_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.lessThanOrEqualTo(from.get(field), value.toString());
        }
    },
    NOT_EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.notEqual(from.get(field), value);
        }
    },
    EQUAL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.equal(from.get(field), value);
        }
    },
    MATCH() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.like(builder.lower(from.get(field)), "%" + value.toString().toLowerCase(Locale.ENGLISH) + "%");
        }
    },
    MATCH_END() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.like(builder.lower(from.get(field)), value.toString().toLowerCase(Locale.ENGLISH) + "%");
        }
    },
    NOT_LIKE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.notLike(from.get(field), value.toString());
        }
    },
    IN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return from.get(field).in(value);
        }
    },
    NOT_IN() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.not(from.get(field).in(value));
        }
    },
    IS_NULL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNull(from.get(field));
        }
    },
    IS_NOT_NULL() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNotNull(from.get(field));
        }
    },
    IS_EMPTY() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isEmpty(from.get(field));
        }
    },
    IS_NOT_EMPTY() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isNotEmpty(from.get(field));
        }
    },
    IS_TRUE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isTrue(from.get(field));
        }
    },
    IS_FALSE() {
        @Override
        public Predicate predicate(CriteriaBuilder builder, String field, Object value, From from) {
            return builder.isFalse(from.get(field));
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
    };

    public abstract Predicate predicate(CriteriaBuilder builder, String field, Object value, From from);
}
