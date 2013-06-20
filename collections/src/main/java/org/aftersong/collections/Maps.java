/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.collections;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert P. Thornton
 */
public class Maps {

	public static <K, V> Map<K, V> newMap() {
		return new HashMap<>();
	}

	protected Maps() {}
}
