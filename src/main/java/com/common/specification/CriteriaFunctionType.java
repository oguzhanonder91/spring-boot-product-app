package com.common.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;

public enum CriteriaFunctionType {
	COUNT {
		@Override
		public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from) {
			return builder.count(from.get(field)).alias(alias);
		}
	},
	SUM {
		@Override
		public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from) {
			return builder.sum(from.get(field)).alias(alias);
		}
	},
	AVG {
		@Override
		public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from) {
			return builder.avg(from.get(field)).alias(alias);
		}
	},
	ABS {
		@Override
		public Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from) {
			return builder.abs(from.get(field)).alias(alias);
		}
	};

	public abstract Selection<?> functionField(CriteriaBuilder builder, String field, String alias, From from);
}