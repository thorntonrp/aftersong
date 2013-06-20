/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.modules.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Robert P. Thornton
 */
public class Lists {

	public static <E> List<E> newList() {
		return new ArrayList<>();
	}

	@SafeVarargs
	public static <E> List<E> newList(E... values) {
		return new ArrayList<>(Arrays.asList(values));
	}

	public static <E> List<E> newList(Collection<E> values) {
		return new ArrayList<>(values);
	}

	protected Lists() {
	}
}
