/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.aftersong.collections.Lists;
import org.aftersong.logging.Logger;

import static org.aftersong.logging.Logger.getLogger;

/**
 *
 * @author Robert P. Thornton
 */
public class PathSearch {

	private static final Logger LOG = getLogger();

	/**
	 * Finds files by file extension, starting from the supplied path.
	 *
	 * @param fromPath the folder to search from
	 * @param extensions extensions of files to include in the results
	 *
	 * @return A list of file URI strings
	 *
	 * @throws IOException if there is an I/O error while searching the file
	 *                     system.
	 */
	public static List<String> search(Path fromPath, String... extensions) throws IOException {
		final List<String> results = Lists.newList();
		FileExtensionHandler handler = new FileExtensionHandler(extensions) {
			@Override
			protected void process(Path file, String fileExtension) {
				LOG.fine("Adding {0} ...", file);
				results.add(file.toUri().toString());
			}
		};
		FileScanner scanner = new FileScanner();
		scanner.setFileHandlers(handler);
		scanner.scan(fromPath);
		return results;
	}

	private PathSearch() {
	}
}
