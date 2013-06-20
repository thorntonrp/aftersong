/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.aftersong.collections.Lists;
import org.aftersong.collections.Sets;
import org.aftersong.logging.Log;
import org.aftersong.logging.Logger;

/**
 *
 * @author Robert P. Thornton
 */
public class DefaultImageResourceScanner extends SimpleFileVisitor<Path> implements ImageResourceScanner {

	private static final Logger LOG = Log.getLogger();

	private List<ImageResource> fileEntries = Lists.newList();
	private Set<String> fileExtensions = Collections.emptySet();

	private Path basePath;

	public void setFileExtensions(String... fileExtensions) {
		this.fileExtensions = Sets.newSet(fileExtensions);
	}

	@Override
	public List<ImageResource> scanFileSystem(Path startPath) {
		this.basePath = startPath;
		try {
			LOG.info("Scanning for images beginning at {0} ...", startPath);
			Files.walkFileTree(startPath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, this);
			Collections.sort(fileEntries, new ImageResourceComparator());
			return fileEntries;
		} catch (IOException ex) {
			LOG.severe(ex);
			return Collections.emptyList();
		}
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
		FileVisitResult result = super.visitFile(path, attrs);
		String fileName = path.getFileName().toString();
		if (fileName.matches(".*\\.(zip|jar)")) {
			visitZipFile(path);
		} else if (fileExtensions.contains(getFileExtension(fileName))) {
			visitImageFile(path);
		} else {
			LOG.fine("Unrecognized file: {0}", path);
		}
		return result;
	}

	public List<ImageResource> getImageResources() {
		return fileEntries;
	}

	private String getFileExtension(String fileName) {
		int extIndex = fileName.lastIndexOf('.');
		if (extIndex > 0 && extIndex < fileName.length() - 1) {
			return fileName.substring(extIndex);
		}
		return null;
	}

	private String getEntryName(ZipEntry entry) {
		String[] pathElements = entry.getName().split("/");
		return pathElements[pathElements.length - 1];
	}

	private void visitImageFile(Path path) {
		Path relativePath = basePath.relativize(path);
		fileEntries.add(new ImageResource(path, relativePath));
	}

	private void visitZipFile(Path path) {
		try {
			scanZipFile(path);
		} catch (ZipException ex) {
			LOG.warning("Invalid zip or jar file: {0}", ex, path);
		} catch (IOException ex) {
			LOG.warning("Failed to read zip or jar file: {0}", ex, path);
		}
	}

	private void visitZipEntry(Path zipFilePath, ZipEntry entry) {
		String zipEntryPath = entry.getName();
		String fileName = getEntryName(entry);
		if (fileExtensions.contains(getFileExtension(fileName))) {
			Path relativeZipFilePath = basePath.relativize(zipFilePath);
			fileEntries.add(new ImageResource(zipFilePath, relativeZipFilePath, zipEntryPath));
		}
	}

	private void scanZipFile(Path path) throws ZipException, IOException {
		try (final ZipFile zipFile = new ZipFile(path.toFile())) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
					visitZipEntry(path, entry);
				}
			}
		}
	}
}
