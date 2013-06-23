/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.testng.annotations.Test;

/**
 *
 * @author Robert P. Thornton
 */
public class TransformsTest {

	@Test
	public void test() throws Exception {
		Application.launch(Main.class);
	}

	public static class Main extends Application {

		@Override
		public void start(Stage primaryStage) throws Exception {
			Parent node = FXMLLoader.load(getClass().getResource("TransformsTest.fxml"));
			Scene scene = new Scene(node);

			Rectangle2D rect = Screen.getPrimary().getVisualBounds();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setWidth(rect.getWidth());
			primaryStage.setHeight(rect.getHeight());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static class Controller {

		@FXML
		private ImageView imageView;

		public void initialize() {
			imageView.setImage(new Image(Paths.get(
					System.getProperty("user.home"), "Pictures", "Spuddy.jpg")
					.toUri().toString(), 400.0, 400.0, true, false));
			Transforms.enableDrag(imageView);
			Transforms.enableZoom(imageView);
			Transforms.enableRotate(imageView);
		}
	}
}
