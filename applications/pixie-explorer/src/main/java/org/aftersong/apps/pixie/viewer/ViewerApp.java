/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooserBuilder;
import javafx.stage.Popup;
import javafx.stage.PopupBuilder;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Robert P. Thornton
 */
public class ViewerApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Viewer.fxml"));

		Scene scene = new Scene(root);
		scene.setFill(new Color(0, 0, 0, .25));
		stage.setScene(scene);
		scene.onKeyPressedProperty().setValue(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case ESCAPE:
						stage.close();
						break;
				}
			}
		});
		maximize(stage);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();

//		File imageFile = chooseImageFile(stage);
//		if (imageFile != null) {
//			Image image = readImage(imageFile);
//			if (image != null) {
//				ImageView imageView = new ImageView(image);
//
//				ScrollPane scrollPane = new ScrollPane();
//				scrollPane.setPannable(true);
//				scrollPane.setContent(imageView);
//				Scene scene = new Scene(scrollPane);
//
//				stage.setScene(scene);
//				stage.show();
//			}
//		}
	}

	private static File chooseImageFile(Stage stage) {
		File initialDirectory = new File(System.getProperty("user.home") + File.separator + "Pictures");
		if (!initialDirectory.isDirectory()) {
			initialDirectory = new File(System.getProperty("user.home"));
		}
		ExtensionFilter extensionFilter = new ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
		FileChooser fileChooser = FileChooserBuilder.create()
				.initialDirectory(initialDirectory)
				.title("Open Image File")
				.extensionFilters(extensionFilter)
				.build();
		return fileChooser.showOpenDialog(stage);
	}

	private static Image readImage(File imageFile) {
		Image image = null;
		try (FileInputStream in = new FileInputStream(imageFile)) {
			image = new Image(in);
		} catch (IOException ex) {
			Popup popup = PopupBuilder.create()
					.content(new Label("Failed to open " + imageFile.getPath() + ": " + ex.getMessage()))
					.build();
		}
		return image;
	}

	private static void maximize(Stage stage) {
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinX());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
	}
}
