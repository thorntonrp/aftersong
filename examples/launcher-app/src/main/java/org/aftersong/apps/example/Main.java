/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.aftersong.modules.logging.Log;

/**
 *
 * @author Robert P. Thornton
 */
public class Main {

	private static final Log LOG = Log.getLogger();

	public static void main(String[] args) throws IOException {
		// Start-up onfiguraation
		String mainClassName = "org.aftersong.pixie.Main";
		Path repository = Paths.get(System.getProperty("user.home"), ".m2", "repository");
		Path[] systemDependencies = {
			Paths.get(System.getProperty("java.home"), "lib", "jfxrt.jar")
		};
		String[] dependencies = {
			"javax.inject:javax.inject:1",
			"aopalliance:aopalliance:1.0",
			"commons-logging:commons-logging:1.1.1",
			"org.aftersong:aftersong-collections:1.0-SNAPSHOT",
			"org.aftersong:aftersong-logging:1.0-SNAPSHOT",
			"org.aftersong.pixie:pixie-viewer:1.0-SNAPSHOT",
			"org.springframework:spring-aop:3.2.3.RELEASE",
			"org.springframework:spring-beans:3.2.3.RELEASE",
			"org.springframework:spring-context:3.2.3.RELEASE",
			"org.springframework:spring-core:3.2.3.RELEASE",
			"org.springframework:spring-expression:3.2.3.RELEASE",
		};

		List<URL> urls = new ArrayList<>();

		for (Path jarPath : systemDependencies) {
			try {
				URL url = jarPath.toUri().toURL();
				urls.add(url);
			} catch (MalformedURLException ex) {
				LOG.severe("Failed to locate dependendy", ex);
			}
		}

		for (String dependency : dependencies) {
			String[] values = dependency.split(":");
			String groupId = values[0];
			String artifactId = values[1];
			String version = values[2];
			String groupPath = groupId.replace('.', '/');
			Path jarPath = Paths.get(repository.toString(), groupPath, artifactId, version, artifactId + '-' + version + ".jar");
			try {
				URL url = jarPath.toUri().toURL();
				urls.add(url);
			} catch (MalformedURLException ex) {
				LOG.severe("Failed to locate dependendy", ex);
				return;
			}
		}

		ClassLoader appClassLoader = new AppClassLoader(urls);
		Class<?> mainClass;
		try {
			mainClass = Class.forName(mainClassName, false, appClassLoader);
		} catch (ClassNotFoundException ex) {
			LOG.severe(ex);
			return;
		}

		Method mainMethod;
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(appClassLoader);
			mainMethod = mainClass.getMethod("main", String[].class);
		} catch (NoSuchMethodException | SecurityException ex) {
			LOG.severe(ex);
			return;
		} finally {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}

		try {
			mainMethod.invoke(null, (Object) args);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			LOG.severe(ex);
		}
	}
}
