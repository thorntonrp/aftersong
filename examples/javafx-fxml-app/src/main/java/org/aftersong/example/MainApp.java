/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.example;

import static javafx.application.Application.launch;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 *
 * @author Robert P. Thornton
 */
public class MainApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		stage.setScene(new Scene(loadFXML()));
	}

	//-- Private Implementation ----------------------------------------------//

	private Parent loadFXML() throws IOException {
		return FXMLLoader.load(getClass().getResource("Main.fxml"));
	}
}
