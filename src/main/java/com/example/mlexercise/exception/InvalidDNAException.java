package com.example.mlexercise.exception;

public class InvalidDNAException extends Exception {

	private static final long serialVersionUID = 6400587275600593625L;

	public InvalidDNAException() {
		super();
	}

	public InvalidDNAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidDNAException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDNAException(String message) {
		super(message);
	}

	public InvalidDNAException(Throwable cause) {
		super(cause);
	}

	
}
