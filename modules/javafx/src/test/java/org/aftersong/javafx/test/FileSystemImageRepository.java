/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.aftersong.collections.Lists;
import org.aftersong.io.FileExtensionHandler;
import org.aftersong.io.FileScanner;
import org.aftersong.logging.Logger;

import static org.aftersong.logging.Logger.getLogger;

/**
 *
 * @author Robert P. Thornton
 */
class FileSystemImageRepository {

	private static final Logger LOG = getLogger();

	private List<String> uris = Lists.newList();

	private List<Path> basePaths;
	private List<String> fileExtensions;
	private boolean compressedFolderScanningEnabled;

	void setBasePaths(Path... basePaths) {
		this.basePaths = Lists.newList(basePaths);
	}

	void setFileExtensions(String... fileExtensions) {
		this.fileExtensions = Lists.newList(fileExtensions);
	}

	void setCompressedFolderScanningEnabled(boolean compressedFolderScanningEnabled) {
		this.compressedFolderScanningEnabled = compressedFolderScanningEnabled;
	}

	void load() throws IOException {
		uris = Lists.newList();
		List<String> scanExtensions = Lists.newList(fileExtensions);
		if (compressedFolderScanningEnabled) {
			scanExtensions.add("zip");
		}
		FileScanner scanner = new FileScanner();
		scanner.setFileHandlers(new FileExtensionHandler() {
			@Override
			protected void process(Path file, String fileExtension) throws IOException {
				if (compressedFolderScanningEnabled && "zip".equals(fileExtension)) {
					scanZipFile(file);
				} else {
					uris.add(file.toUri().toString());
				}
			}

			private void scanZipFile(Path path) throws IOException {
				LOG.info("scanning {0}", path);
				try {
					try (final ZipFile zipFile = new ZipFile(path.toFile())) {
						Enumeration<? extends ZipEntry> entries = zipFile.entries();
						while (entries.hasMoreElements()) {
							ZipEntry entry = entries.nextElement();
							if (!entry.isDirectory()) {
								visitZipEntry(path, entry);
							}
						}
					}
				} catch (IllegalArgumentException | IOException ex) {
					LOG.fine(ex);
					LOG.warning("Failed to scan {0}. Reason: {1}",
							path, ex.toString());
				}
			}

			private void visitZipEntry(Path zipFilePath, ZipEntry entry) {
				String fileName = getEntryName(entry);
				if (fileExtensions.contains(getFileExtension(fileName))) {
					uris.add("jar:" + zipFilePath + "!/" + entry.getName());
				}
			}

			private String getEntryName(ZipEntry entry) {
				String[] pathElements = entry.getName().split("/");
				return pathElements[pathElements.length - 1];
			}
		}.extensions(scanExtensions));
		for (Path path : basePaths) {
			try {
				scanner.scan(path);
			} catch (IOException ex) {
				LOG.fine(ex);
				LOG.warning("Failed to scan base path {0}. Reason: {1}",
						path, ex.toString());
			}
		}
		Collections.sort(uris);
		int i = 0;
		for (String uri : uris) {
			System.out.println(++i + ": " + uri);
		}
	}

	CircularIterator<String> iterator() {
		return new CircularIterator<>(uris);
	}

	public static void main(String[] args) throws IOException {
		FileSystemImageRepository repository = new FileSystemImageRepository();
		repository.setBasePaths(
					Paths.get(System.getProperty("user.home"), "Pictures"),
					Paths.get(System.getProperty("user.home"), "Documents"),
					Paths.get(System.getProperty("user.home"), "Music"),
					Paths.get("D:/Pictures/Collections")
				);
		repository.setFileExtensions("jpg", "png", "bmp");
		repository.setCompressedFolderScanningEnabled(true);
		repository.load();
	}
}
