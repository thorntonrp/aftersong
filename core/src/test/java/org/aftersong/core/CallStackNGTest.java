/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.core;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 *
 * @author Robert P. Thornton
 */
public class CallStackNGTest {

	@Test
	public void testGetCallingClass() {
		class Inner {
			void test() {
				Class<?> caller = CallStack.getCallingClass();
				assertEquals(caller, CallStackNGTest.class);
			}
		}
		new Inner().test();
	}
}