/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Robert P. Thornton
 */
public class DefaultImageResourceRepository implements ImageResourceRepository {

	private ImageResourceScanner scanner;
	private Path baseImagePath;

	public DefaultImageResourceRepository() {
		baseImagePath = Paths.get(System.getProperty("user.home"), "Pictures");
	}

	@Override
	public List<ImageResource> getImageResources() {
		return scanner.scanFileSystem(baseImagePath);
	}

	public void setImageResourceScanner(ImageResourceScanner imageResourceScanner) {
		this.scanner = imageResourceScanner;
	}
}
