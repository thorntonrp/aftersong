/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author Robert Thornton
 */
public class Objects {

	public static boolean equal(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}

	public static <E> Iterable<E> in(E... array) {
		return array == null ? Collections.<E>emptyList() : Arrays.asList(array);
	}

	public static <E> Iterable<E> in(Collection<E> collection) {
		return collection == null ? Collections.<E>emptyList() : collection;
	}

	public static <K, V> Iterable<Map.Entry<K, V>> in(Map<K, V> map) {
		return map == null ? Collections.<K, V>emptyMap().entrySet() : map.entrySet();
	}
}
