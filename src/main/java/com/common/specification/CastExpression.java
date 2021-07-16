package com.common.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 * @author Oğuzhan ÖNDER
 * @date 16.07.2021 - 13:27
 */
public final class CastExpression {

    private CastExpression() {
    }

    public static <T> Expression<T> cast(Object value, CriteriaBuilder builder) {
        return builder.literal((T) value);
    }


}
