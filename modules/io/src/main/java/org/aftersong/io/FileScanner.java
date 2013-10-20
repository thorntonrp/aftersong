/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aftersong.logging.Logger;

import static org.aftersong.logging.Logger.getLogger;

/**
 *
 * @author Robert P. Thornton
 */
public class FileScanner {

	private static final Logger LOG = getLogger();

	private List<FileHandler> fileHandlers = new ArrayList<>();

	private final FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			FileVisitResult result = super.visitFile(file, attrs);
			for (FileHandler handler : fileHandlers) {
				handler.process(file);
			}
			return result;
		}
	};

	public void setFileHandlers(FileHandler... handlers) {
		this.fileHandlers = new ArrayList<>(Arrays.asList(handlers));
	}

	public void setFileHandlers(List<FileHandler> handlers) {
		this.fileHandlers = new ArrayList<>(handlers);
	}

	public void scan(Path basePath) throws IOException {
		Files.walkFileTree(basePath, fileVisitor);
	}
}
