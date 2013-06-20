/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 *
 * @author Robert P. Thornton
 */
public class Main extends Application implements Constants {

	//-- Static Members ------------------------------------------------------//

	public static void main(String[] args) {
		launch(args);
	}

	//-- Instance Members ----------------------------------------------------//

	private GenericApplicationContext rootContext;
	private AnnotationConfigApplicationContext childContext;

	@Override
	public void start(final Stage stage) throws Exception {
		startupSpring(stage);
		showStage();
	}

	@Override
	public void stop() throws Exception {
		shutdownSpring();
		super.stop();
	}

	//-- Private Implementation ----------------------------------------------//

	private void startupSpring(Stage stage) {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerSingleton("primaryStage", stage);
		rootContext = new GenericApplicationContext(factory);
		childContext = new AnnotationConfigApplicationContext(MainConfiguration.class);
		childContext.setParent(rootContext);
		rootContext.refresh();
	}

	private void shutdownSpring() {
		rootContext.close();
		childContext.close();
	}

	private void showStage() {
		childContext.getBean(Stage.class).show();
	}
}
