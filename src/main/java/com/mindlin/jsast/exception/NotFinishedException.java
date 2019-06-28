package com.mindlin.jsast.exception;

import com.mindlin.nautilus.fs.SourcePosition;

public class NotFinishedException extends RuntimeException {

	public NotFinishedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotFinishedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotFinishedException(String message, SourcePosition position) {
		super(message);
	}

	public NotFinishedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotFinishedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
