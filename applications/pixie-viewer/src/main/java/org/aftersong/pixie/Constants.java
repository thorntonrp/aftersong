/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

/**
 *
 * @author Robert Thornton
 */
public interface Constants {

	String TITLE = "Pixie Image Viewer";

	String RESOURCE_PACKAGE = Constants.class.getPackage().getName();

	String RESOURCE_FOLDER = Constants.class.getPackage().getName().replace('.', '/');

	String MESSAGES = RESOURCE_PACKAGE + ".message";
}
