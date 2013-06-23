/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Robert Thornton
 */
public class PixieApplication extends Application {

	//-- Static Members ------------------------------------------------------//

	private static PixieApplication application;

	public static void main(String[] args) {
		launch(args);
	}

	//-- Instance Members ----------------------------------------------------//

	private ExecutorService executor;

	@Override
	public void start(Stage primaryStage) throws Exception {
		application = this;
		executor = Executors.newFixedThreadPool(4);
		Scene scene = new Scene(new PixieRootPane());
		Rectangle2D rect = Screen.getPrimary().getVisualBounds();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.getIcons().addAll(getWindowIcons());
		primaryStage.setWidth(rect.getWidth());
		primaryStage.setHeight(rect.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		executor.shutdown();
		super.stop();
	}

	public static void submitTask(Runnable task) {
		application.executor.submit(task);
	}

	//-- Private Implementation ----------------------------------------------//

	private List<Image> getWindowIcons() {
		List<Image> iconImages = Arrays.asList(
				new Image(getClass().getResource("icon-16.png").toString()),
				new Image(getClass().getResource("icon-32.png").toString()),
				new Image(getClass().getResource("icon-48.png").toString()));
		return iconImages;
	}
}
