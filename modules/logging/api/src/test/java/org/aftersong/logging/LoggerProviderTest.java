/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.logging;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 *
 * @author Robert P. Thornton
 */
public class LoggerProviderTest {

	@Test
	public void test() {
		try {
			Logger.getLogger();
		} catch (ExceptionInInitializerError ex) {
			assertEquals(IllegalStateException.class, ex.getCause().getClass());
			assertTrue(ex.getCause().getMessage().startsWith(
					"Failed to find implementation of " + LoggerProvider.class.getName()));
		}
	}
}
