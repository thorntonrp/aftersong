/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @param <T> The result type of the processed file
 *
 * @author Robert P. Thornton
 */
public abstract class FileExtensionHandler<T> implements FileHandler<T> {

	private Set<String> extensions = Collections.emptySet();

	public FileExtensionHandler() {
	}

	public FileExtensionHandler(String... extensions) {
		setExtensions(extensions);
	}

	public FileExtensionHandler(Collection<String> extensions) {
		setExtensions(extensions);
	}

	/**
	 * Sets the file extensions of files to be processed by this handler. Do
	 * not include the leading dot in the file extensions.
	 *
	 * @param extensions The file extensions of files to be handled.
	 */
	public final void setExtensions(Collection<String> extensions) {
		this.extensions = new HashSet<>(extensions);
	}

	/**
	 * Sets the file extensions of files to be processed by this handler. Do
	 * not include the leading dot in the file extensions.
	 *
	 * @param extensions The file extensions of files to be handled.
	 */
	public final void setExtensions(String... extensions) {
		this.extensions = new HashSet<>(Arrays.asList(extensions));
	}

	@Override
	public final T process(Path file) {
		String ext = getFileExtension(file.getFileName().toString());
		if (extensions.contains(ext)) {
			return process(file, ext);
		}
		return null;
	}

	protected abstract T process(Path file, String fileExtension);

	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf('.') + 1;
		if (index > 0 && index < fileName.length()) {
			return fileName.substring(index);
		}
		return null;
	}
}
