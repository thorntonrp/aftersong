/*
 * Copyright 2013 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie.file;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import javafx.util.Callback;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageFileFilter implements FileFilter {

	private static final Pattern DEFAULT_IMAGE_FILE_PATTERN =
			Pattern.compile("(?i).*\\.(jpe?g|png)");

	private Pattern pattern = DEFAULT_IMAGE_FILE_PATTERN;
	
	private boolean acceptHidden;
	private boolean acceptDirectories;

	private Callback<FileItem, Void> callback;
	
	public ImageFileFilter pattern(Pattern pattern) {
		this.pattern = pattern;
		return this;
	}

	public ImageFileFilter acceptHidden(boolean acceptHidden) {
		this.acceptHidden = acceptHidden;
		return this;
	}

	public ImageFileFilter acceptDirectories(boolean acceptDirectories) {
		this.acceptDirectories = acceptDirectories;
		return this;
	}
	
	public ImageFileFilter callback(Callback<FileItem, Void> callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public boolean accept(File fileOrFolder) {
		if (acceptHidden || !isHidden(fileOrFolder)) {
			boolean isDirectory = fileOrFolder.isDirectory();
			if (acceptDirectories || !isDirectory) {
				if (isDirectory || hasImageFileExtension(fileOrFolder)) {
					if (callback != null) {
						callback.call(new FileItem(fileOrFolder, isDirectory));
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean isHidden(File file) {
		return file.isHidden() || file.getName().charAt(0) == '.';
	}

	private boolean hasImageFileExtension(File file) {
		return pattern.matcher(file.getName()).matches();
	}
}
