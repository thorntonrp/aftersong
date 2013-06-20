/*
 * Copyright 2013 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.file;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Robert P. Thornton
 */
public class FileItem implements Comparable<FileItem> {

	private final File file;

	private final boolean directory;

	public FileItem(File file, boolean directory) {
		if (file == null) {
			throw new IllegalArgumentException("The file argument cannot be null.");
		}
		this.file = file;
		this.directory = directory;
	}

	public File getFile() {
		return file;
	}

	public URL getURL() throws MalformedURLException {
		return file.toURI().toURL();
	}

	public boolean isDirectory() {
		return directory;
	}

	@Override
	public String toString() {
		return file.getName();
	}

	@Override
	public int compareTo(FileItem that) {
		boolean d1 = this.isDirectory();
		boolean d2 = that.isDirectory();
		if (d1 == d2) {
			return this.getFile().compareTo(that.getFile());
		} else if (d1) {
			return -1;
		} else {
			return 1;
		}
	}
}
