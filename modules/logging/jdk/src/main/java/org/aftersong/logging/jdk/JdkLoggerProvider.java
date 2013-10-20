/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging.jdk;

import org.aftersong.logging.Logger;
import org.aftersong.logging.LoggerProvider;

/**
 *
 * @author Robert P. Thornton
 */
public class JdkLoggerProvider implements LoggerProvider {

	private Logger global;

	@Override
	public Logger getNamedLogger(String loggerName) {
		return JdkLogger.getLogger(loggerName);
	}

	@Override
	public Logger getGlobalLogger() {
		if (global == null) {
			global = new JdkLogger(java.util.logging.Logger.getGlobal());
		}
		return global;
	}

	@Override
	public Logger getAnonymousLogger() {
		return new JdkLogger(java.util.logging.Logger.getAnonymousLogger());
	}
}
