/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;

import static java.text.MessageFormat.format;

/**
 * An interface to be implemented by all {@code Logger} implementations.
 *
 * @author Robert P. Thornton
 */
public abstract class Logger {

	private static final LoggerProvider PROVIDER;
	static {
		ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class);
		Iterator<LoggerProvider> providers = loader.iterator();
		if (providers.hasNext()) {
			PROVIDER = providers.next();
		} else {
			IllegalStateException ex = new IllegalStateException(format(
					"Failed to find implementation of {0}. This should be " +
					"defined in META-INF/services/{0} of the jar providing " +
					"the implementation.", LoggerProvider.class.getName()));
			java.util.logging.Logger.getGlobal().log(Level.SEVERE, ex.toString(), ex);
			throw ex;
		}
	}

	private static final Logger GLOBAL = PROVIDER.getGlobalLogger();

	/**
	 * Returns a logger named for the calling class. If the calling class cannot
	 * be determined due to security restrictions, an anonymous logger will be
	 * returned instead.
	 *
	 * @return a logger for use by the calling class
	 */
	public static Logger getLogger() {
		Class<?> caller = CallStackProvider.getInstance().getCaller();
		if (caller != null) {
			return PROVIDER.getNamedLogger(caller.getName());
		} else {
			GLOBAL.log(Level.WARNING,
					"Failed to obtain a logger by determining the calling " +
					"class name. Please use {0}.getLogger(Class) instead. " +
					"An anonymous logger will be created instead.",
					Logger.class.getName());
		}
		return PROVIDER.getAnonymousLogger();
	}

	/**
	 * Returns a logger named for the supplied package.
	 *
	 * @param forPackage the package to be used in naming the logger
	 *
	 * @return a logger for use in the context of the supplied package
	 */
	public static Logger getLogger(Package forPackage) {
		return PROVIDER.getNamedLogger(forPackage.getName());
	}

	/**
	 * Returns a logger named for the supplied class.
	 *
	 * @param forClass the class to be used in naming the logger
	 *
	 * @return a logger for use in the context of the supplied class
	 */
	public static Logger getLogger(Class<?> forClass) {
		return PROVIDER.getNamedLogger(forClass.getName());
	}

	/**
	 * Returns a {@link Logger} for a named logging context.
	 *
	 * @param name the name of the logger
	 *
	 * @return a logger for use in the named logging context
	 */
	public static Logger getLogger(String name) {
		return PROVIDER.getNamedLogger(name);
	}

	//-- General logging operations ------------------------------------------//

	/**
	 * Logs the supplied message and {@link Throwable} at the specified logging
	 * level.
	 *
	 * @param level the target logging level
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void log(Level level, Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the specified logging
	 * level.
	 *
	 * @param level the target logging level
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void log(Level level, Object msg, Throwable thrown, Object... params);

	//-- "configuration" level logging operations ----------------------------//

	/**
	 * Logs the supplied message at the {@link Level#CONFIG config} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void config(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#CONFIG config} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void config(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#CONFIG config}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void config(Throwable thrown);

	//-- "fine" debugging level logging operations ---------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINE fine} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void fine(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINE fine} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void fine(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINE fine}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void fine(Throwable thrown);

	//-- "finer" debugging level logging operations --------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINER finer} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void finer(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINER finer} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void finer(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINER finer}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void finer(Throwable thrown);

	//-- "finest" debugging level logging operations -------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINEST finest} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void finest(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINEST finest} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void finest(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINEST finest}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void finest(Throwable thrown);

	//-- "information" level logging operations ------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#INFO info} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void info(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#INFO info} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void info(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#INFO info}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void info(Throwable thrown);

	//-- "severe" level logging operations -----------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#SEVERE severe} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void severe(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#SEVERE severe} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void severe(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#SEVERE severe}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void severe(Throwable thrown);

	//-- "warning" level logging operations ----------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#WARNING warning} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	public abstract void warning(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#WARNING warning} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	public abstract void warning(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#WARNING warning}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	public abstract void warning(Throwable thrown);

	//-- Logger properties ---------------------------------------------------//

	/**
	 * Returns the logger to which all log operations on this object are
	 * delegated.
	 *
	 * @return the delegate logger
	 */
	public abstract java.util.logging.Logger getDelegate();

	/**
	 * Returns the effective logging level in use by the delegate logger or its
	 * parent loggers.
	 *
	 * @return the effective logging level
	 *
	 * @see java.util.logging.Logger#getLevel()
	 * @see java.util.logging.Logger#getParent()
	 */
	public abstract Level getEffectiveLevel();

	/**
	 * Returns the name of the delegate logger.
	 *
	 * @return the name of the delegate logger
	 *
	 * @see java.util.logging.Logger#getName()
	 */
	public abstract String getName();

	/**
	 * Returns a handler for logging uncaught exceptions.
	 *
	 * @return a handler for uncaught exception logging
	 */
	public abstract UncaughtExceptionHandler getUncaughtExceptionLogger();

	//-- Logger utility operations -------------------------------------------//

	/**
	 *
	 * @param level the logging level to be tested against
	 *
	 * @return {@code true} if the supplied logging level is active.
	 *
	 * @see java.util.logging.Logger#isLoggable(Level);
	 */
	public abstract boolean isLoggable(Level level);

	//-- Private Implementation ----------------------------------------------//

	private static class CallStackProvider extends SecurityManager {

		private static final CallStackProvider INSTANCE = new CallStackProvider();

		static CallStackProvider getInstance() {
			return INSTANCE;
		}

		Class<?>[] getCallStack() {
			return super.getClassContext();
		}

		Class<?> getCaller() {
			for (Class<?> caller : getCallStack()) {
				String callerName = caller.getName().split("\\$")[0];
				if (!callerName.equals(LoggerProvider.class.getName())) {
					return caller;
				}
			}
			return null;
		}
	}
}
