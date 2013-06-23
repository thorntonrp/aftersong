/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Robert Thornton
 */
public class Pixie extends Application {

	//-- Static Members ------------------------------------------------------//

	public static void main(String[] args) {
		launch(args);
	}

	//-- Instance Members ----------------------------------------------------//

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(new PixieRootPane());
		Rectangle2D rect = Screen.getPrimary().getVisualBounds();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setWidth(rect.getWidth());
		primaryStage.setHeight(rect.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
