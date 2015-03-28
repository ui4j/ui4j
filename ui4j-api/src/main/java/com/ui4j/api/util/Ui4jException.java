package com.ui4j.api.util;

public class Ui4jException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Ui4jException(String message) {
		super(message);
	}

	public Ui4jException(Exception e) {
		super(e);
	}
}
