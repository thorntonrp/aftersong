/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

/**
 * A provider for {@link Logger} instances.
 *
 * @author Robert P. Thornton
 */
public interface LoggerProvider {

	/**
	 *
	 * @param loggerName
	 * @return
	 */
	Logger getNamedLogger(String loggerName);

	/**
	 * Returns a logger suitable for logging global messages.
	 *
	 * @return a logger suitable for global use
	 */
	Logger getGlobalLogger();

	/**
	 * Returns a logger suitable for use by untrusted code.
	 *
	 * @return a logger suitable for anonymous use
	 */
	Logger getAnonymousLogger();
}
