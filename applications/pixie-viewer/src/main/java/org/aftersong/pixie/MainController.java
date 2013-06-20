/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 *
 * @author Robert P. Thornton
 */
public class MainController {

	private static double MARGIN = 10;

	@FXML
	private Pane rootPane;

	@FXML
	private VBox leftPane;

	@FXML
	private VBox rightPane;

	@FXML
	private HBox topPane;

	@FXML
	private HBox bottomPane;

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Scene scene = rootPane.getScene();
				Stage stage = (Stage) scene.getWindow();

				Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
				stage.getIcons().addAll(loadIconImages());
				stage.setTitle("Image Browser");
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setWidth(bounds.getWidth());
				stage.setHeight(bounds.getHeight());

				stage.show();

				leftPane.setTranslateX(-leftPane.getWidth() + MARGIN);
				rightPane.setTranslateX(rightPane.getWidth() - MARGIN);
				topPane.setTranslateY(-topPane.getHeight() + MARGIN);
				bottomPane.setTranslateY(topPane.getHeight() - MARGIN);
				leftPane.setStyle("-fx-background-color: transparent");
				rightPane.setStyle("-fx-background-color: transparent");
				topPane.setStyle("-fx-background-color: transparent");
				bottomPane.setStyle("-fx-background-color: transparent");
			}
		});
	}

	@FXML
	void show(MouseEvent event) {
		Pane pane = (Pane) event.getTarget();
		pane.setStyle("-fx-background-color: #EFEFEFFF;");
		pane.toFront();
		final TranslateTransition showTransition = new TranslateTransition();
		showTransition.setDuration(new Duration(200));
		showTransition.setToX(0);
		showTransition.setToY(0);
		showTransition.setNode(pane);
		showTransition.play();
		showTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showTransition.getNode().setStyle("-fx-background-color: #EFEFEFFF");
			}
		});
	}

	@FXML
	void hide(MouseEvent event) {
		Pane pane = (Pane) event.getTarget();
//		Scene scene = pane.getScene();
//		double x = event.getSceneX();
//		double y = event.getSceneY();
//		double width = scene.getWidth();
//		double height = scene.getHeight();
//		if (x < 0 || x > width || y < 0 || y > height) {
//			return;
//		}
		final TranslateTransition hideTransition = new TranslateTransition();
		hideTransition.setDuration(new Duration(200));
		hideTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				hideTransition.getNode().setStyle("-fx-background-color: transparent");
			}
		});
		hideTransition.setNode(pane);
		switch (pane.getId()) {
			case "leftPane": {
				hideTransition.setToX(-pane.getWidth() + MARGIN);
				hideTransition.setToY(0);
				break;
			}
			case "rightPane": {
				hideTransition.setToX(pane.getWidth() - MARGIN);
				hideTransition.setToY(0);
				break;
			}
			case "topPane": {
				hideTransition.setToX(0);
				hideTransition.setToY(-pane.getHeight() + MARGIN);
				break;
			}
			case "bottomPane": {
				hideTransition.setToX(0);
				hideTransition.setToY(pane.getHeight() - MARGIN);
				break;
			}
		}
		hideTransition.play();
	}

	private List<Image> loadIconImages() {
		int[] iconSizes = {16, 24, 32, 48, 64, 128, 256};
		List<Image> images = new ArrayList<>();
		for (int size : iconSizes) {
			images.add(new Image(getClass().getResourceAsStream("icon-" + size + ".png")));
		}
		return images;
	}
}
