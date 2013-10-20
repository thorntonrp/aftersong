/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author Robert P. Thornton
 */
public interface FileHandler {

	public void process(Path path) throws IOException;
}
