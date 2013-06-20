/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.modules.example;

import java.util.Collection;
import java.util.Map;

/**
 * Provides assertion-based argument validation against expected values.
 * Operations that succeed will do nothing while operations that fail will throw
 * an unchecked {@link AssertionError}.
 *
 * @author Robert P. Thornton
 */
public class Assert {

	protected Assert() {}

	/**
	 * Tests whether the supplied array of values is null or has zero length.
	 *
	 * @param values the array of values to be tested
	 */
	public static void isEmpty(Object[] values) {
		if (!Check.isEmpty(values)) {
			throw new AssertionError("expected empty array");
		}
	}

	/**
	 * Tests whether the supplied array of values is non-null and holds one or
	 * more elements.
	 *
	 * @param values the array of values to be tested
	 */
	public static void isNotEmpty(Object[] values) {
		if (Check.isEmpty(values)) {
			throw new AssertionError("expected non-empty array");
		}
	}

	/**
	 * Tests whether the supplied collection of values is non-null and holds one
	 * or more elements.
	 *
	 * @param values the collection of values to be tested
	 */
	public static void isEmpty(Collection<?> values) {
		if (!Check.isEmpty(values)) {
			throw new AssertionError("expected empty collection");
		}
	}

	/**
	 * Tests whether the supplied collection of values is non-null and holds one
	 * or more elements.
	 *
	 * @param values the collection of values to be tested
	 */
	public static void isNotEmpty(Collection<?> values) {
		if (Check.isEmpty(values)) {
			throw new AssertionError("expected non-empty collection");
		}
	}

	/**
	 * Tests whether the supplied map of values is null or has a zero entries.
	 *
	 * @param values the map of values to be tested
	 */
	public static void isEmpty(Map<?, ?> values) {
		if (!Check.isEmpty(values)) {
			throw new AssertionError("expected empty map");
		}
	}

	/**
	 * Tests whether the supplied map of values is non-null and holds one or
	 * more entries.
	 *
	 * @param values the map of values to be tested
	 */
	public static void isNotEmpty(Map<?, ?> values) {
		if (Check.isEmpty(values)) {
			throw new AssertionError("expected non-empty map");
		}
	}

	/**
	 * Tests whether the supplied String value is null or has a zero length.
	 *
	 * @param value the value to be tested
	 */
	public static void isEmpty(String value) {
		if (!Check.isEmpty(value)) {
			throw new AssertionError("expected empty string");
		}
	}

	/**
	 * Tests whether the supplied String value is non-null and has one or more
	 * characters.
	 *
	 * @param value the value to be tested
	 */
	public static void isNotEmpty(String value) {
		if (Check.isEmpty(value)) {
			throw new AssertionError("expected non-empty String");
		}
	}

	/**
	 * Tests whether the supplied String value is null or consists only of
	 * whitespace characters.
	 *
	 * @param value the value to be tested
	 */
	public static void isBlank(String value) {
		if (!Check.isBlank(value)) {
			throw new AssertionError("expected blank string");
		}
	}

	/**
	 * Tests whether the supplied String value is non-null and consists of one
	 * or more non-whitespace characters.
	 *
	 * @param value the value to be tested
	 */
	public static void isNotBlank(String value) {
		if (Check.isBlank(value)) {
			throw new AssertionError("expected non-blank string");
		}
	}

	/**
	 * The whether the supplied argument is {@code null}.
	 *
	 * @param value the value to be tested
	 */
	public static void isNull(Object value) {
		if (value != null) {
			throw new AssertionError("expected null value");
		}
	}

	/**
	 * Tests whether the supplied argument is non-null.
	 *
	 * @param value the value to be tested
	 */
	public static void isNotNull(Object value) {
		if (value == null) {
			throw new AssertionError("expected non-null value");
		}
	}

	/**
	 * Tests whether any element of the supplied variable argument array is
	 * {@code null}.
	 *
	 * @param values the array of values to be tested
	 */
	public static void isAnyNull(Object... values) {
		for (Object o : values) {
			if (o == null) {
				throw new AssertionError("expected non-null arguments");
			}
		}
	}

	/**
	 * Tests whether the supplied value is {@code true}.
	 *
	 * @param value the value to be tested
	 */
	public static void isTrue(boolean value) {
		if (!value) {
			throw new AssertionError("expected true");
		}
	}

	/**
	 * Tests whether the supplied value is {@code false}.
	 *
	 * @param value the value to be tested
	 */
	public static void isFalse(boolean value) {
		if (value) {
			throw new AssertionError("expected false");
		}
	}

	/**
	 * Tests the supplied values for equality. If both values are {@code null}
	 * this result will be true. If only one value is {@code null}, the result
	 * will be false. If both values are non-null, this operation will invoke
	 * {@link Object#equals(Object) on the first argument, supplying the second
	 * argument as the parameter.
	 *
	 * @param value1 the first argument to be compared with the second
	 * @param value2 the second argument to be compared against the first
	 */
	public static void equal(Object value1, Object value2) {
		if (value1 == value2) {
			// do nothing
		} else if (value1 == null || value2 == null) {
			throw new AssertionError("arguments are not equal");
		} else if (value1.equals(value2)) {
			throw new AssertionError("arguments are not equal");
		}
	}
}
