/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T> The result type of the processed file
 *
 * @author Robert P. Thornton
 */
public class FileScanner<T> {

	private List<FileHandler<? extends T>> fileHandlers = new ArrayList<>();

	public void setFileHandlers(List<FileHandler<? extends T>> handlers) {
		this.fileHandlers = new ArrayList<>(handlers);
	}

	public List<T> scan(Path basePath) throws IOException {
		final List<T> results = new ArrayList<>();
		Files.walkFileTree(basePath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				FileVisitResult result = super.visitFile(file, attrs);
				for (FileHandler<? extends T> handler : fileHandlers) {
					T value = handler.process(file);
					if (value != null) {
						results.add(value);
					}
				}
				return result;
			}
		});
		return results;
	}
}
