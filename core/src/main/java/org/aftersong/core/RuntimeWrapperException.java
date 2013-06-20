/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.core;

/**
 *
 * @author Robert P. Thornton
 */
public class RuntimeWrapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeWrapperException() {
	}

	public RuntimeWrapperException(String message) {
		super(message);
	}

	public RuntimeWrapperException(Throwable cause) {
		super(cause);
	}

	public RuntimeWrapperException(String message, Throwable cause) {
		super(message, cause);
	}
}
