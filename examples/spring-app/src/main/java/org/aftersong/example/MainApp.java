/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.example;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *
 * @author Robert P. Thornton
 */
@Singleton
@Component
public class MainApp {

	public static void main(String[] args) {
		ApplicationContext appContext = new AnnotationConfigApplicationContext(
				MainConfiguration.class);
		MainApp main = appContext.getBean(MainApp.class);
		main.startup();
	}

	@Inject
	private Provider<MainFrame> mainFrameProvider;

	public void startup() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrameProvider.get().setVisible(true);
			}
		});
	}
}
