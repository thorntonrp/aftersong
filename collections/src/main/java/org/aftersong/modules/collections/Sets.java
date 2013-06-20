/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.modules.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Robert P. Thornton
 */
public class Sets {

	public static <E> Set<E> newSet() {
		return new HashSet<>();
	}

	@SafeVarargs
	public static <E> Set<E> newSet(E... values) {
		return new HashSet<>(Arrays.asList(values));
	}

	public static <E> Set<E> newSet(Collection<E> values) {
		return new HashSet<>(values);
	}

	public static <E> Set<E> newLinkedSet() {
		return new LinkedHashSet<>();
	}

	@SafeVarargs
	public static <E> Set<E> newLinkedSet(E... values) {
		return new LinkedHashSet<>(Arrays.asList(values));
	}

	public static <E> Set<E> newLinkedSet(Collection<E> values) {
		return new LinkedHashSet<>(values);
	}

	protected Sets() {
	}
}
