/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.core;

/**
 *
 * @author Robert P. Thornton
 */
public class Threads {

	public static void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException ex) {
			throw new RuntimeWrapperException(ex);
		}
	}

	private Threads() {
	}
}
