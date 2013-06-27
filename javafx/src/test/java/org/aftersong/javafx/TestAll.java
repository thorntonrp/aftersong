/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.aftersong.io.PathSearch;
import org.testng.annotations.Test;

/**
 *
 * @author Robert P. Thornton
 */
public class TestAll {

	@Test
	public void test() throws Exception {
		Application.launch(Main.class);
	}

	public static class Main extends Application {

		@Override
		public void start(Stage primaryStage) throws Exception {
			SlidingBorderPane rootPane = new SlidingBorderPane();
			rootPane.setId("rootPane");
			rootPane.setBackgroundImage(new Image(getClass().getResourceAsStream("background.png")));
			rootPane.getStylesheets().add(getClass().getResource("RootPane.css").toString());

			ImageView imageCanvas = new ImageView();

			LabelBuilder<?> label = LabelBuilder.create()
					.prefWidth(160).prefHeight(120)
					.alignment(Pos.CENTER);

			rootPane.setTop(label.text("TOP").build());
			rootPane.setRight(label.text("RIGHT").build());
			rootPane.setBottom(label.text("BOTTOM").build());
			rootPane.setLeft(label.text("LEFT").build());

			rootPane.setCenter(imageCanvas);

			Controller controller = new Controller();
			controller.imageView = imageCanvas;
			controller.rootPane = rootPane;
			controller.initialize();

			Scene scene = new Scene(rootPane);

			Rectangle2D rect = Screen.getPrimary().getVisualBounds();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setWidth(rect.getWidth());
			primaryStage.setHeight(rect.getHeight());
			//primaryStage.setWidth(rect.getWidth() * 0.75);
			//primaryStage.setHeight(rect.getHeight() * 0.75);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static class Controller {

		private static final double SCALE_FACTOR = 1.075;

		@FXML
		private SlidingBorderPane rootPane;

		@FXML
		private ImageView imageView;

		private ImageModel imageModel;

		public void initialize() throws Exception {
			imageModel = new ImageModel();

			Transforms.enableDrag(imageView);
			Transforms.enableZoom(imageView);
			Transforms.enableRotate(imageView);

			if (imageModel.isNotEmpty()) {
				imageView.setImage(new Image(imageModel.current()));
				rootPane.setOnScroll(new ScrollNavigationHandler());
			}

			SizeChangeHandler sizeChangeHandler = new SizeChangeHandler();
			rootPane.widthProperty().addListener(sizeChangeHandler);
			rootPane.heightProperty().addListener(sizeChangeHandler);
		}

		private double calculateScale(Image image) {
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();
			double containerWidth = rootPane.getWidth();
			double containerHeight = rootPane.getHeight();
			double scaledWidth = imageWidth;
			double scaledHeight = imageHeight;
			double scale = 1.0;
			if (containerWidth > 0 && containerHeight > 0) { // to prevent infinite loop
				while (scaledWidth > containerWidth || scaledHeight > containerHeight) {
					scale /= SCALE_FACTOR;
					scaledWidth = imageWidth * scale;
					scaledHeight = imageHeight * scale;
				}
			}
			return scale;
		}

		private class ScrollNavigationHandler implements EventHandler<ScrollEvent> {

			@Override
			public void handle(ScrollEvent event) {
				if (!event.isAltDown() && !event.isControlDown() &&
					!event.isMetaDown() && !event.isShiftDown() &&
					!event.isShortcutDown()) {

					String uri;
					if (event.getDeltaY() > 0) {
						uri = imageModel.previous();
					} else {
						uri = imageModel.next();
					}

					Image image = new Image(uri);

					double scale = calculateScale(image);

					imageView.setScaleX(scale);
					imageView.setScaleY(scale);
					imageView.setRotate(0.0);
					imageView.setImage(image);
				}
			}
		}

		private class SizeChangeHandler implements ChangeListener<Number> {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double scale = calculateScale(imageView.getImage());
				imageView.setScaleX(scale);
				imageView.setScaleY(scale);
			}
		}
	}

	private static class ImageModel {

		private final List<String> uris;

		private int index = 0;

		ImageModel() throws IOException {
			Path basePath = Paths.get(System.getProperty("user.home"), "Pictures");
			this.uris = PathSearch.search(basePath, "jpg", "png");
		}

		String current() {
			return uris.get(index);
		}

		String next() {
			index++;
			if (index == uris.size()) {
				index = 0;
			}
			return current();
		}

		String previous() {
			index--;
			if (index == -1) {
				index = uris.size() - 1;
			}
			return current();
		}

		private boolean isNotEmpty() {
			return !uris.isEmpty();
		}
	}
}
