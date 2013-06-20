/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author Robert P. Thornton
 */
public class Gallery extends Application {

	private static Gallery gallery;

	public static Gallery getGallery() {
		return gallery;
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private Stage primaryStage;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage stage) throws Exception {
		List<Image> iconImages = Arrays.asList(
					new Image(getClass().getResourceAsStream("icon-16.png")),
					new Image(getClass().getResourceAsStream("icon-24.png")),
					new Image(getClass().getResourceAsStream("icon-32.png")),
					new Image(getClass().getResourceAsStream("icon-48.png")),
					new Image(getClass().getResourceAsStream("icon-64.png")),
					new Image(getClass().getResourceAsStream("icon-128.png")),
					new Image(getClass().getResourceAsStream("icon-256.png"))
				);
		primaryStage = stage;
		primaryStage.getIcons().addAll(iconImages);
		gallery = this;

		Parent root = FXMLLoader.load(
				getClass().getResource("Gallery.fxml"),
				ResourceBundle.getBundle(getClass().getPackage().getName() + ".messages"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		try {
			super.stop();
		} finally {
			gallery = null;
		}
	}
}
