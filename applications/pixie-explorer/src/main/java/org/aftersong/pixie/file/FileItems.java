/*
 * Copyright 2013 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.util.Callback;

/**
 *
 * @author Robert P. Thornton
 */
public class FileItems {

	public static List<FileItem> list(FileItem directoryItem) {
		final List<FileItem> filteredResults = new ArrayList<>();
		Callback<FileItem, Void> callback = new Callback<FileItem, Void>() {
			@Override
			public Void call(FileItem fileItem) {
				filteredResults.add(fileItem);
				return null;
			}
		};
		ImageFileFilter filter = new ImageFileFilter().acceptDirectories(true).callback(callback);
		directoryItem.getFile().listFiles(filter);
		Collections.sort(filteredResults);
		return filteredResults;
	}

	//-- Private Operations --------------------------------------------------//
	
	private FileItems() {
	}
}
