package com.common.specification;

/**
 * @author Ozgun Murtaza BAKICI
 *
 * @date 7 Nis 2021 10:30:01
 */

public class SearchBetween {

	private Object from;
	private Object to;


	public SearchBetween() {
	}

	public SearchBetween(Object from, Object to) {
		this.from = from;
		this.to = to;
	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}
}
