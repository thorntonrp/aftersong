/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author Robert P. Thornton
 */
public interface ImageResourceScanner {

	List<ImageResource> scanFileSystem(Path startPath);
}
