/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx.test;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <T> the element type
 *
 * @author Robert P. Thornton
 */
public class CircularIterator<T> implements Iterator<T> {

	private static final long serialVersionUID = 1L;

	private final List<T> values;

	private int index = -1;

	public CircularIterator(List<T> values) {
		this.values = values;
	}

	public T current() {
		return values.get(index);
	}

	@Override
	public T next() {
		index++;
		if (index >= values.size()) {
			index = 0;
		}
		return current();
	}

	public T previous() {
		index--;
		if (index < 0) {
			index = values.size() - 1;
		}
		return current();
	}

	@Override
	public void remove() {
		values.remove(index);
		index--;
	}

	@Override
	public boolean hasNext() {
		return !values.isEmpty();
	}
}
