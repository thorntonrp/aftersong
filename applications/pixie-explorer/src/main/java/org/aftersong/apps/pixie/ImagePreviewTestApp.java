/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author Robert P. Thornton
 */
public class ImagePreviewTestApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<Image> iconImages = Arrays.asList(
					new Image(getClass().getResourceAsStream("icon-16.png")),
					new Image(getClass().getResourceAsStream("icon-24.png")),
					new Image(getClass().getResourceAsStream("icon-32.png")),
					new Image(getClass().getResourceAsStream("icon-48.png")),
					new Image(getClass().getResourceAsStream("icon-64.png")),
					new Image(getClass().getResourceAsStream("icon-128.png")),
					new Image(getClass().getResourceAsStream("icon-256.png"))
				);
		primaryStage.getIcons().addAll(iconImages);
		File initialDirectory = new File(System.getProperty("user.home") + File.separator + "Pictures");
		if (!initialDirectory.isDirectory()) {
			initialDirectory = new File(System.getProperty("user.home"));
		}
		List<Path> imagePaths = listImagePaths(initialDirectory.getPath());
		ImagePreviewPane thumbnailPreview = new ImagePreviewPane(imagePaths);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setContent(thumbnailPreview);
		scrollPane.setPrefWidth(1280);
		scrollPane.setPrefHeight(720);

		Scene scene = new Scene(scrollPane);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private List<Path> listImagePaths(String startingPath) {
		Path path = Paths.get(startingPath);
		File previewFolder = path.toFile();
		List<Path> previewImages = new ArrayList<>();
		for (File f : previewFolder.listFiles()) {
			if (f.isFile() && f.getName().matches(".*\\.(jpg|png)$")) {
				previewImages.add(f.toPath());
			}
		}
		return previewImages;
	}
}
