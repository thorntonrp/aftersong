/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.nio.file.Path;

/**
 *
 * @param <T> The result type of the processed file
 *
 * @author Robert P. Thornton
 */
public interface FileHandler<T> {

	public T process(Path path);
}
