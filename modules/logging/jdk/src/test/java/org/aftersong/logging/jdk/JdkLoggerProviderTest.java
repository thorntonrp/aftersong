/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging.jdk;

import java.util.logging.Level;

import org.aftersong.logging.jdk.JdkLogger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
/**
 *
 * @author Robert P. Thornton
 */
public class JdkLoggerProviderTest {

	@BeforeClass
	public void initializeLogging() {
		JdkLogger.initializeLogging();
	}

	@Test
	public void test_get_byCallingClass() {
		JdkLogger log = JdkLogger.getLogger();
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), JdkLoggerProviderTest.class.getName());
	}

	@Test
	public void test_get_byClass() {
		JdkLogger log = JdkLogger.getLogger(JdkLoggerProviderTest.class);
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), JdkLoggerProviderTest.class.getName());
	}

	@Test
	public void test_get_byName() {
		JdkLogger log = JdkLogger.getLogger(JdkLoggerProviderTest.class.getName());
		assertEquals(log.getEffectiveLevel(), Level.FINEST);
		assertEquals(log.getName(), JdkLoggerProviderTest.class.getName());
	}
}
