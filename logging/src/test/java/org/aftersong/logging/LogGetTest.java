/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

import static org.testng.Assert.*;

import java.util.logging.Level;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Robert P. Thornton
 */
public class LogGetTest {

	@BeforeClass
	public void initializeLogging() {
		Log.initializeLogging();
	}

	@Test
	public void test_get_byCallingClass() {
		Log log = Log.getLogger();
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), LogGetTest.class.getName());
	}

	@Test
	public void test_get_byClass() {
		Log log = Log.getLogger(LogGetTest.class);
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), LogGetTest.class.getName());
	}

	@Test
	public void test_get_byName() {
		Log log = Log.getLogger(LogGetTest.class.getName());
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), LogGetTest.class.getName());
	}
}
