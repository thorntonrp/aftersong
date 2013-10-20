/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging.jdk;

import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.aftersong.logging.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
/**
 *
 * @author Robert P. Thornton
 */
public class JdkLoggerTest {

	private final LogHandler handler = new LogHandler();
	private final Throwable thrown = new Throwable();

	private Logger log;

	@BeforeClass
	public void initializeLogging() {
		JdkLogger.initializeLogging();
		log = JdkLogger.getLogger("jdk-test-logger");
		for (Handler h : log.getDelegate().getHandlers()) {
			log.getDelegate().removeHandler(h);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		log.getDelegate().setLevel(Level.ALL);
		log.getDelegate().addHandler(handler);
	}

	@AfterMethod
	public void afterMethod() {
		log.getDelegate().removeHandler(handler);
	}

	@Test
	public void testFinest() {
		log.finest("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.finest("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testFiner() {
		log.finer("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.finer("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testFine() {
		log.fine("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.fine("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testInfo() {
		log.info("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.info("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testConfig() {
		log.config("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.config("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testWarning() {
		log.warning("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.warning("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	@Test
	public void testSevere() {
		log.severe("test {0} {1} {2}", 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertNull(handler.getThrown());

		log.severe("test {0} {1} {2}", thrown, 1, 2, 3);
		assertEquals(handler.getFormattedMessage(), "test 1 2 3");
		assertEquals(handler.getThrown(), thrown);
	}

	public static class LogHandler extends java.util.logging.Handler {

		private LogRecord record;

		public String getFormattedMessage() {
			assert record != null;
			if (record.getParameters() == null) {
				return record.getMessage();
			} else {
				return MessageFormat.format(record.getMessage(), record.getParameters());
			}
		}

		public Throwable getThrown() {
			assert record != null;
			return record.getThrown();
		}

		@Override
		public void publish(LogRecord record) {
			this.record = record;
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() throws SecurityException {
		}
	}
}
