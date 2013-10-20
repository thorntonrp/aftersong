/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Robert P. Thornton
 */
public abstract class FileExtensionHandler implements FileHandler {

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

	public final FileExtensionHandler extensions(Collection<String> extensions) {
		setExtensions(extensions);
		return this;
	}

	public final FileExtensionHandler extensions(String... extensions) {
		setExtensions(extensions);
		return this;
	}

	@Override
	public final void process(Path file) throws IOException {
		String ext = getFileExtension(file.getFileName().toString());
		if (extensions.contains(ext)) {
			process(file, ext);
		}
	}

	protected abstract void process(Path file, String fileExtension) throws IOException;

	protected String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf('.') + 1;
		if (index > 0 && index < fileName.length()) {
			return fileName.substring(index);
		}
		return null;
	}
}
