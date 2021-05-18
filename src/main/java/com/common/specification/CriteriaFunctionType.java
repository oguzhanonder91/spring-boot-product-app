package com.common.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.SimpleCase;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;
import java.util.Arrays;

public enum CriteriaFunctionType {
    COUNT {
        @Override
        public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens) {
            return builder.count(from.get(field)).alias(alias);
        }
    },
    SUM {
        @Override
        public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens) {
            return builder.sum(from.get(field)).alias(alias);
        }
    },
    AVG {
        @Override
        public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens) {
            return builder.avg(from.get(field)).alias(alias);
        }
    },
    ABS {
        @Override
        public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens) {
            return builder.abs(from.get(field)).alias(alias);
        }
    },
    CASE {
        @Override
        public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens) {
            SimpleCase simpleCase = builder.selectCase(from.get(field));
            for (Object selectCaseWhen : Arrays.asList(whens)) {
                if (selectCaseWhen instanceof SelectCaseWhen) {
                    SelectCaseWhen caseWhen = (SelectCaseWhen) selectCaseWhen;
                    simpleCase.when(caseWhen.getWhen(), caseWhen.getResult());
                }
            }
            simpleCase.otherwise(from.get(field));
            simpleCase.alias(alias);
            return simpleCase;
        }
    };

    public abstract Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from, Object... whens);
}