/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.example;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author Robert P. Thornton
 */
public class AppClassLoader extends URLClassLoader {

	public AppClassLoader(List<URL> urls) {
		super(urls.toArray(new URL[urls.size()]));
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		return super.loadClass(name, resolve);
//		Class<?> loadedClass = findLoadedClass(name);
//		if (loadedClass == null) {
//			loadedClass = findClass(name);
//			if (loadedClass == null) {
//			}
//		}
//		return loadedClass;
	}

	@Override
	protected String findLibrary(String libname) {
		return super.findLibrary(libname);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println(name);
		return super.findClass(name);
	}

	@Override
	public URL findResource(String name) {
		return super.findResource(name);
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		return super.findResources(name);
	}
}
