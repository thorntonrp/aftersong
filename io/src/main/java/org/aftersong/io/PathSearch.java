/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.aftersong.logging.Log;
import org.aftersong.logging.Logger;

/**
 *
 * @author Robert P. Thornton
 */
public class PathSearch {

	private static final Logger LOG = Log.getLogger();

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
		FileScanner<String> scanner = new FileScanner<>();
		FileExtensionHandler<String> handler = new FileExtensionHandler<String>(extensions) {
			@Override
			protected String process(Path file, String fileExtension) {
				LOG.fine("Adding {0} ...", file);
				return file.toUri().toString();
			}
		};
		scanner.setFileHandlers(Arrays.<FileHandler<? extends String>>asList(handler));
		return scanner.scan(fromPath);
	}

	private PathSearch() {
	}
}
