/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageResource {

	// Cached hash code
	private Integer hash;

	// Image location fields
	private final String imageUri;
	private final Path imagePath;

	// Archive location fields
	private final Path archivePath;
	private final String archiveEntry;

	private ImageResourceMetaData metaData;

	public ImageResource(Path imagePath, Path imageRelativePath) {
		this.imageUri = imagePath.toUri().toString();
		this.imagePath = imagePath;
		this.archivePath = null;
		this.archiveEntry = null;
	}

	public ImageResource(Path archivePath, Path archiveRelativePath, String archiveEntry) {
		this.imageUri = "jar:" + archivePath.toUri().toString() + "!/" + archiveEntry;
		this.imagePath = Paths.get(archivePath.toString(), archiveEntry);
		this.archivePath = archivePath;
		this.archiveEntry = archiveEntry;
	}

	public ImageResource(String imageUri) {
		this.imageUri = imageUri;
		this.imagePath = Paths.get(URI.create(imageUri));
		int entryIndex = imageUri.indexOf("!/");
		if (imageUri.startsWith("jar:file:") && entryIndex > 0) {
			this.archivePath = Paths.get(URI.create(imageUri.substring("jar:".length(), entryIndex)));
			this.archiveEntry = imageUri.substring(entryIndex + "!/".length());
		} else {
			this.archivePath = null;
			this.archiveEntry = null;
		}
	}

	public ImageResourceMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(ImageResourceMetaData metaData) {
		if (this.metaData != null) {
			throw new IllegalStateException("meta data already initialized");
		}
		this.metaData = metaData;
	}

	public Path getImagePath() {
		return imagePath;
	}

	public String getImageUri() {
		return imageUri;
	}

	public String getArchiveEntry() {
		return archiveEntry;
	}

	public Path getArchivePath() {
		return archivePath;
	}

	@Override
	public String toString() {
		if (archiveEntry != null) {
			return archivePath + "!/" + archiveEntry;
		} else if (archivePath != null) {
			return archivePath.toString();
		} else {
			return imageUri;
		}
	}

	@Override
	public int hashCode() {
		if (hash == null) {
			hash = 5;
			hash = 37 * hash + Objects.hashCode(this.imageUri);
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof ImageResource) {
			return getImageUri().equals(((ImageResource) obj).getImageUri());
		}
		return false;
	}
}
