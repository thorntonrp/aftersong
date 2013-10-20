/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.aftersong.logging.Logger;

import static org.aftersong.logging.Logger.getLogger;

/**
 *
 * @author Robert P. Thornton
 */
public class TestApp extends Application {

	//-- Static Members ------------------------------------------------------//

	private static final Logger LOG = getLogger();

	public static void main(String[] args) {
		launch(args);
	}

	//-- Instance Members ----------------------------------------------------//

	private ExecutorService executor;

	@Override
	public void start(Stage primaryStage) throws IOException {
		executor = Executors.newFixedThreadPool(4);

		// Initialize the model, view, and controller
		Future<CircularIterator<String>> futureModel = loadModel();
		ImageCollectionView mainView = new ImageCollectionView();
		TestController controller = new TestController();
		controller.setRootPane(mainView);
		controller.setExecutor(executor);
		controller.initialize(futureModel);

		prepareStage(primaryStage, mainView);
	}

	@Override
	public void stop() throws Exception {
		executor.shutdown();
		super.stop();
	}

	//-- Private Implementation ----------------------------------------------//

	private Future<CircularIterator<String>> loadModel() {
		return executor.submit(new Callable<CircularIterator<String>>() {
			@Override
			public CircularIterator<String> call() throws IOException {
				FileSystemImageRepository repository = new FileSystemImageRepository();
				repository.setBasePaths(
						Paths.get(System.getProperty("user.home"), "Pictures"),
						Paths.get(System.getProperty("user.home"), "Documents"),
						Paths.get(System.getProperty("user.home"), "Music"),
						Paths.get("D:/Pictures/Collections")
					);
				repository.setFileExtensions("jpg", "png", "bmp");
				repository.setCompressedFolderScanningEnabled(true);
				repository.load();
				return repository.iterator();
			}
		});
	}

	private void prepareStage(Stage stage, Pane rootPane) {
		Scene scene = new Scene(rootPane);
		Rectangle2D rect = Screen.getPrimary().getVisualBounds();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setWidth(rect.getWidth());
		stage.setHeight(rect.getHeight());
		//stage.setWidth(rect.getWidth() * 0.75);
		//stage.setHeight(rect.getHeight() * 0.75);
		stage.getIcons().addAll(getWindowIcons());
		stage.setScene(scene);
		stage.show();
	}

	private List<Image> getWindowIcons() {
		List<Image> iconImages = Arrays.asList(
				new Image(getClass().getResource("icon-16.png").toString()),
				new Image(getClass().getResource("icon-32.png").toString()),
				new Image(getClass().getResource("icon-48.png").toString()));
		return iconImages;
	}
}
