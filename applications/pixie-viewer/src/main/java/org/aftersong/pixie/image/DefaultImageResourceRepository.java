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

	private DefaultImageResourceScanner scanner;
	private Path baseImagePath;

	public DefaultImageResourceRepository() {
		baseImagePath = Paths.get(System.getProperty("user.home"), "Pictures");
		scanner = new DefaultImageResourceScanner();
		scanner.setFileExtensions(".jpg", ".png", ".bmp", ".gif", ".jpeg");
	}

	@Override
	public List<ImageResource> getImageResources() {
		return scanner.scanFileSystem(baseImagePath);
	}
}
