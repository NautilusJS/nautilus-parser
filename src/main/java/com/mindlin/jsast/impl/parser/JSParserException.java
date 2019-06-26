package com.mindlin.jsast.impl.parser;

public class JSParserException extends RuntimeException {
	private static final long serialVersionUID = 4187331265454224988L;

	public JSParserException() {
		super();
	}

	public JSParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JSParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSParserException(String message) {
		super(message);
	}

	public JSParserException(Throwable cause) {
		super(cause);
	}
}
