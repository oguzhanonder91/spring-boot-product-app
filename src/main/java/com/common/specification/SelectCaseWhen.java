package com.common.specification;

public class SelectCaseWhen {
	private Object when;
	private Object result;

	public SelectCaseWhen() {
	}

	public SelectCaseWhen(Object when, Object result) {
		this.when = when;
		this.result = result;
	}

	public Object getWhen() {
		return when;
	}

	public void setWhen(Object when) {
		this.when = when;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
