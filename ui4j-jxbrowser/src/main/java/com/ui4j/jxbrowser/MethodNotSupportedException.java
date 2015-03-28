package com.ui4j.jxbrowser;

import com.ui4j.api.util.Ui4jException;

public class MethodNotSupportedException extends Ui4jException {

	private static final long serialVersionUID = 5104953239710455585L;

	public MethodNotSupportedException() {
		super("MethodNotSupportedException");
	}

	public MethodNotSupportedException(Exception e) {
		super(e);
	}
}
