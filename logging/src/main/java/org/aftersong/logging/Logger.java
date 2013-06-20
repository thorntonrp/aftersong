/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;

/**
 *
 * @author Robert P. Thornton
 */
public interface Logger {

	//-- General logging operations ------------------------------------------//

	/**
	 * Logs the supplied message and {@link Throwable} at the specified logging
	 * level.
	 *
	 * @param level the target logging level
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void log(Level level, Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the specified logging
	 * level.
	 *
	 * @param level the target logging level
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void log(Level level, Object msg, Throwable thrown, Object... params);

	//-- "configuration" level logging operations ----------------------------//

	/**
	 * Logs the supplied message at the {@link Level#CONFIG config} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void config(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#CONFIG config} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void config(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#CONFIG config}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void config(Throwable thrown);

	//-- "fine" debugging level logging operations ---------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINE fine} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void fine(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINE fine} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void fine(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINE fine}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void fine(Throwable thrown);

	//-- "finer" debugging level logging operations --------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINER finer} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void finer(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINER finer} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void finer(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINER finer}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void finer(Throwable thrown);

	//-- "finest" debugging level logging operations -------------------------//

	/**
	 * Logs the supplied message at the {@link Level#FINEST finest} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void finest(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#FINEST finest} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void finest(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#FINEST finest}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void finest(Throwable thrown);

	//-- "information" level logging operations ------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#INFO info} logging level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void info(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#INFO info} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void info(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#INFO info}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void info(Throwable thrown);

	//-- "severe" level logging operations -----------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#SEVERE severe} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void severe(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#SEVERE severe} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void severe(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#SEVERE severe}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void severe(Throwable thrown);

	//-- "warning" level logging operations ----------------------------------//

	/**
	 * Logs the supplied message at the {@link Level#WARNING warning} logging
	 * level.
	 *
	 * @param msg the message to be logged
	 * @param params parameters to the message
	 */
	void warning(Object msg, Object... params);

	/**
	 * Logs the supplied message and {@link Throwable} at the
	 * {@link Level#WARNING warning} logging level.
	 *
	 * @param msg the message to be logged
	 * @param thrown the {@code Throwable} to be logged
	 * @param params parameters to the message
	 */
	void warning(Object msg, Throwable thrown, Object... params);

	/**
	 * Logs the supplied {@link Throwable} at the {@link Level#WARNING warning}
	 * logging level.
	 *
	 * @param thrown the {@code Throwable} to be logged
	 */
	void warning(Throwable thrown);

	//-- Logger properties ---------------------------------------------------//

	/**
	 * Returns the logger to which all log operations on this object are
	 * delegated.
	 *
	 * @return the delegate logger
	 */
	java.util.logging.Logger getDelegate();

	/**
	 * Returns the effective logging level in use by the delegate logger or its
	 * parent loggers.
	 *
	 * @return the effective logging level
	 *
	 * @see java.util.logging.Logger#getLevel()
	 * @see java.util.logging.Logger#getParent()
	 */
	Level getEffectiveLevel();

	/**
	 * Returns the name of the delegate logger.
	 *
	 * @return the name of the delegate logger
	 *
	 * @see java.util.logging.Logger#getName()
	 */
	String getName();

	/**
	 * Returns a handler for logging uncaught exceptions.
	 *
	 * @return a handler for uncaught exception logging
	 */
	UncaughtExceptionHandler getUncaughtExceptionLogger();

	//-- Logger utility operations -------------------------------------------//

	/**
	 *
	 * @param level the logging level to be tested against
	 *
	 * @return {@code true} if the supplied logging level is active.
	 *
	 * @see java.util.logging.Logger#isLoggable(Level);
	 */
	boolean isLoggable(Level level);
}
