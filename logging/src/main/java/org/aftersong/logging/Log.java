/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * A custom {@code Logger} that delegates all logging to a wrapped
 * {@link java.util.logging.Logger} instance. This class wraps the delegate
 * logger to provide additional useful features and operations, namely the
 * following:
 * <ul>
 * <li>Provides the static {@link #initializeLogging()} operation that will
 * initialize logging from a "logging.properties" resource on the class
 * path.</li>
 * <li>Provides the static {@link #getLogger()} operation for obtaining a logger
 * having the name of the calling class.</li>
 * <li>Allows passing an object as the message, assuring that it will only
 * be converted to a string when the log record is created. This can improve
 * performance when logging expensive string representations of objects
 * (this behavior can also be obtained by passing the object as a message
 * parameter instead of as the message itself).</li>
 * <li>Provides variable argument parameters on any log method that accepts a
 * message.</li>
 * <li>Provides overloaded logging operations named for each log level that
 * can accept both {@link Throwable} and variable message argument
 * parameters.</li>
 * </ul>
 * To modify the underlying logger, the delegate logger may be obtained via
 * {@link #getDelegate()}.
 *
 * @author Robert P. Thornton
 */
public class Log implements Logger {

	private static final String LOGGING_RESOURCE = "logging.properties";

	/**
	 * Initialize logging from a logging properties resource returned from the
	 * class loader of the calling class. By default the name of the logging
	 * properties resource is "logging.properties".
	 */
	public static void initializeLogging() {
		initializeLogging(LOGGING_RESOURCE);
	}

	/**
	 * Initialize logging from the specified logging properties resource as
	 * returned from the class loader of the calling class.
	 *
	 * @param loggingResourceName
	 */
	public static void initializeLogging(String loggingResourceName) {
		String loggingConfigClass = System.getProperty("java.util.logging.config.class");
		String loggingConfigFile = System.getProperty("java.util.logging.config.file");
		if (loggingConfigClass == null && loggingConfigFile == null) {
			Class<?> caller = CallStackProvider.getInstance().getCaller();
			if (caller != null) {
				ClassLoader loader = caller.getClassLoader();
				InputStream in = loader.getResourceAsStream(loggingResourceName);
				if (in != null) {
					try {
						LogManager.getLogManager().readConfiguration(in);
					} catch (IOException | SecurityException ex) {
						java.util.logging.Logger.getLogger(Log.class.getName()).log(Level.SEVERE,
								"Failed to initialized loggging from " +
								"classpath  resource \"{0}\": {1}",
								new Object[] { loggingResourceName, ex });
					} finally {
						try {
							in.close();
						} catch (IOException ex) {
							java.util.logging.Logger.getLogger(Log.class.getName()).log(Level.SEVERE,
									"Failed to close resource: {0}", loggingResourceName);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a logger having the inferred name of the calling class. If the
	 * calling class cannot be determined (most likely due to security
	 * restrictions), an anonymous logger will be returned instead.
	 *
	 * @return a logger named for the calling class
	 */
	public static Log getLogger() {
		Class<?> caller = CallStackProvider.getInstance().getCaller();
		if (caller != null) {
			return new Log(java.util.logging.Logger.getLogger(caller.getName()));
		} else {
			java.util.logging.Logger.getLogger(Log.class.getName()).log(Level.WARNING,
					"Failed to obtain a logger by determining the calling " +
					"class name. Please use {0}.get(Class forClass) instead. " +
					"An anonymous logger will be created instead.",
					Log.class.getName());
		}
		return new Log(java.util.logging.Logger.getAnonymousLogger());
	}

	/**
	 * Returns a logger having the name of the supplied class.
	 *
	 * @param forClass the class from which to derive the logger name
	 *
	 * @return a logger named for the supplied class
	 */
	public static Log getLogger(Class<?> forClass) {
		return new Log(java.util.logging.Logger.getLogger(forClass.getName()));
	}

	/**
	 * Returns a logger having the name of the supplied package.
	 *
	 * @param forPackage the package from which to derived the logger name
	 *
	 * @return a logger named for the supplied package
	 */
	public static Log getLogger(Package forPackage) {
		return new Log(java.util.logging.Logger.getLogger(forPackage.getName()));
	}

	/**
	 * Returns a logger having the supplied name.
	 *
	 * @param loggerName the desired logger name
	 *
	 * @return a logger having the supplied name
	 */
	public static Log getLogger(String loggerName) {
		return new Log(java.util.logging.Logger.getLogger(loggerName));
	}

	@SuppressWarnings({"NonConstantLogger"})
	private final java.util.logging.Logger delegate;

	protected Log(java.util.logging.Logger logger) {
		delegate = logger;
	}

	//-- Log properties --//

	/**
	 * Returns the logger to which all log operations on this object are
	 * delegated.
	 *
	 * @return the delegate logger
	 */
	@Override
	public java.util.logging.Logger getDelegate() {
		return delegate;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public Level getEffectiveLevel() {
		Level level = delegate.getLevel();
		java.util.logging.Logger tempLogger = delegate.getParent();
		while (level == null && tempLogger != null) {
			level = tempLogger.getLevel();
			tempLogger = tempLogger.getParent();
		}
		return level;
	}

	//-- UncaughtExceptionHandler implementation --//

	@Override
	public UncaughtExceptionHandler getUncaughtExceptionLogger() {
		return new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log(Level.SEVERE, "Uncaught exception from thread {0}: {1}", t.getName(), e, e);
			}
		};
	}

	//-- Additional pass-through operations --//

	@Override
	public boolean isLoggable(Level level) {
		return delegate.isLoggable(level);
	}

	//-- "configuration" log operations --//

	@Override
	public void config(Object msg, Object... params) {
		log(Level.CONFIG, msg, params);
	}

	@Override
	public void config(Object msg, Throwable thrown, Object... params) {
		log(Level.CONFIG, msg, thrown, params);
	}

	@Override
	public void config(Throwable thrown) {
		config(thrown, thrown);
	}

	//-- "fine" debug level log operations --//

	@Override
	public void fine(Object msg, Object... params) {
		log(Level.FINE, msg, params);
	}

	@Override
	public void fine(Object msg, Throwable thrown, Object... params) {
		log(Level.FINE, msg, thrown, params);
	}

	@Override
	public void fine(Throwable thrown) {
		fine(thrown, thrown);
	}

	//-- "finer" debug level log operations --//

	@Override
	public void finer(Object msg, Object... params) {
		log(Level.FINER, msg, params);
	}

	@Override
	public void finer(Object msg, Throwable thrown, Object... params) {
		log(Level.FINER, msg, thrown, params);
	}

	@Override
	public void finer(Throwable thrown) {
		finer(thrown, thrown);
	}

	//-- "finest" debug level log operations --//

	@Override
	public void finest(Object msg, Object... params) {
		log(Level.FINEST, msg, params);
	}

	@Override
	public void finest(Object msg, Throwable thrown, Object... params) {
		log(Level.FINEST, msg, thrown, params);
	}

	@Override
	public void finest(Throwable thrown) {
		finest(thrown, thrown);
	}

	//-- "informational" log operations --//

	@Override
	public void info(Object msg, Object... params) {
		log(Level.INFO, msg, params);
	}

	@Override
	public void info(Object msg, Throwable thrown, Object... params) {
		log(Level.INFO, msg, thrown, params);
	}

	@Override
	public void info(Throwable thrown) {
		info(thrown, thrown);
	}

	//-- "severe" log operations --//

	@Override
	public void severe(Object msg, Object... params) {
		log(Level.SEVERE, msg, params);
	}

	@Override
	public void severe(Object msg, Throwable thrown, Object... params) {
		log(Level.SEVERE, msg, thrown, params);
	}

	@Override
	public void severe(Throwable thrown) {
		severe(thrown, thrown);
	}

	//-- "warning" log operations --//

	@Override
	public void warning(Object msg, Object... params) {
		log(Level.WARNING, msg, params);
	}

	@Override
	public void warning(Object msg, Throwable thrown, Object... params) {
		log(Level.WARNING, msg, thrown, params);
	}

	@Override
	public void warning(Throwable thrown) {
		warning(thrown, thrown);
	}

	//-- Generic "log" operations --//

	@Override
	public void log(Level level, Object msg, Object... params) {
		log(level, msg, null, params);
	}

	@Override
	public void log(Level level, Object msg, Throwable thrown, Object... params) {
		if (delegate.isLoggable(level)) {
			delegate.log(createLogRecord(level, msg, thrown, params));
		}
	}

	//-- Private Operations --------------------------------------------------//

	private LogRecord createLogRecord(Level level, Object msg, Throwable throwable, Object... params) {
		LogRecord logRecord = new LogRecord(level, String.valueOf(msg));
		logRecord.setParameters(params);
		logRecord.setThrown(throwable);
		setClassAndMethod(logRecord);
		logRecord.setLoggerName(delegate.getName());
		return logRecord;
	}

	private void setClassAndMethod(LogRecord logRecord) {
		StackTraceElement caller = getCallingStackTraceElement();
		if (caller != null) {
			logRecord.setSourceClassName(caller.getClassName());
			logRecord.setSourceMethodName(caller.getMethodName());
		}
	}

	private StackTraceElement getCallingStackTraceElement() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String logClassName = Log.class.getName();
			int index;
			for (index = 0; index < stackTrace.length; index++) {
				if (logClassName.equals(stackTrace[index].getClassName().split("\\$")[0])) {
					break;
				}
			}
			for (; index < stackTrace.length; index++) {
				if (!logClassName.equals(stackTrace[index].getClassName().split("\\$")[0])) {
					break;
				}
			}
			return index < stackTrace.length ? stackTrace[index] : null;
		} catch (SecurityException ex) {
			return null;
		}
	}

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
				if (!callerName.equals(Log.class.getName())) {
					return caller;
				}
			}
			return null;
		}
	}
}
