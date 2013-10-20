/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.validation;

import java.util.Collection;
import java.util.Map;

/**
 * Provides boolean result argument validation. Argument tests that succeed
 * will return {@code true} while tests that fail will return {@code false}.
 *
 * @author Robert P. Thornton
 */
public class Check {

	protected Check() {}

	/**
	 * Tests whether the supplied array of values is null or has zero length.
	 *
	 * @param values the array of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isEmpty(Object[] values) {
		return values == null || values.length == 0;
	}

	/**
	 * Tests whether the supplied array of values is non-null and holds one or
	 * more elements.
	 *
	 * @param values the array of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isNotEmpty(Object[] values) {
		return !isEmpty(values);
	}

	/**
	 * Tests whether the supplied collection of values is null or has a zero
	 * elements.
	 *
	 * @param values the collection of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isEmpty(Collection<?> values) {
		return values == null || values.isEmpty();
	}

	/**
	 * Tests whether the supplied collection of values is non-null and holds one
	 * or more elements.
	 *
	 * @param values the collection of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isNotEmpty(Collection<?> values) {
		return !isEmpty(values);
	}

	/**
	 * Tests whether the supplied map of values is null or has a zero entries.
	 *
	 * @param values the map of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isEmpty(Map<?, ?> values) {
		return values == null || values.isEmpty();
	}

	/**
	 * Tests whether the supplied map of values is non-null and holds one or
	 * more entries.
	 *
	 * @param values the map of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isNotEmpty(Map<?, ?> values) {
		return !isEmpty(values);
	}

	/**
	 * Tests whether the supplied String value is null or has a zero length.
	 *
	 * @param value the value to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0;
	}

	/**
	 * Tests whether the supplied String value is non-null and has one or more
	 * characters.
	 *
	 * @param value the value to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	/**
	 * Tests whether the supplied String value is null or consists only of
	 * whitespace characters.
	 *
	 * @param value the value to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * Tests whether the supplied String value is non-null and consists of one
	 * or more non-whitespace characters.
	 *
	 * @param value the value to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}

	/**
	 * Tests whether any element of the supplied variable argument array is
	 * {@link null}.
	 *
	 * @param values the array of values to be tested
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean isAnyNull(Object... values) {
		for (Object value : values) {
			if (value == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests the supplied values for equality. If both values are {@code null}
	 * this result will be true. If only one value is {@code null}, the result
	 * will be false. If both values are non-null, this operation will invoke
	 * {@link Object#equals(Object) on the first argument, supplying the second
	 * argument as the parameter.
	 *
	 * @param value1 the first value to be compared to the second
	 * @param value2 the second value to be compared against the first
	 *
	 * @return {@code true} if the test succeeded
	 */
	public static boolean equal(Object value1, Object value2) {
		if (value1 == value2) {
			return true;
		} else if (value1 == null || value2 == null) {
			return false;
		}
		return value1.equals(value2);
	}
}
