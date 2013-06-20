/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;

import org.aftersong.pixie.events.ImageChangedEvent;
import org.aftersong.pixie.image.ImageResource;
import org.aftersong.pixie.image.ImageResourceMetaData;
import org.aftersong.pixie.image.ImageResourceMetaDataRepository;
import org.aftersong.pixie.image.ImageResourceRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Robert P. Thornton
 */
@Lazy
@Controller
public class ImageController {

	@Inject
	private ExecutorService executor;

	@Inject
	private Provider<Stage> primaryStage;

	@Inject
	private ImageCanvasModel imageCanvasModel;
/*
	@Inject
	private ImageResourceRepository imageResourceRepository;
*/
	@Inject
	private ImageResourceMetaDataRepository metaDataRepository;

	@FXML
	private ImageCanvas imageCanvas;
/*
	@FXML
	private TextField namePatternField;
*/
	private Double offsetX;
	private Double offsetY;

//	@FXML
	public void initialize() {
		imageCanvas.setModel(imageCanvasModel);
		imageCanvas.setExecutorService(executor);
		imageCanvas.setImageResourceMetaDataRepository(metaDataRepository);/*
		imageCanvas.addEventHandler(ImageChangedEvent.IMAGE_CHANGED, new EventHandler<ImageChangedEvent>() {
			@Override
			public void handle(ImageChangedEvent event) {
				ImageResource imageResource = event.getImageResource();
				ImageResourceMetaData metaData = imageResource.getMetaData();
				String newTitle = "Pixie Viewer";
				if (imageResource.getArchivePath() != null) {
					newTitle += " - " + imageResource.getArchivePath()
							 +  " > " + imageResource.getArchiveEntry();
				} else {
					newTitle += " - " + imageResource.getImagePath();
				}
				int scalePercent = (int) Math.round(metaData.getScale() * 100);
				newTitle += " (" + scalePercent + "%)";
				primaryStage.get().setTitle(newTitle);
			}
		});*/
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				Stage stage = primaryStage.get();
//				stage.show();
				primaryStage.get().widthProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						imageCanvas.refreshImage();
					}
				});
				imageCanvas.loadNextImage();
			}
		});
	}

	@FXML
	public void handleScrollEvent(ScrollEvent event) {
		if (event.isControlDown()) {
			if (event.getDeltaY() > 0) {
				imageCanvas.scaleImageUp();
			} else {
				imageCanvas.scaleImageDown();
			}
		} else if (event.isShiftDown()) {
			if (event.getDeltaY() > 0) {
				imageCanvas.loadPreviousFolderImage();
			} else {
				imageCanvas.loadNextFolderImage();
			}
		} else if (event.isAltDown()) {
			if (event.getDeltaY() > 0) {
				imageCanvas.rotateLeft();
			} else {
				imageCanvas.rotateRight();
			}
		} else {
			if (event.getDeltaY() > 0) {
				imageCanvas.loadPreviousImage();
			} else {
				imageCanvas.loadNextImage();
			}
		}
	}

	@FXML
	public void handleKeyPressedEvent(KeyEvent event) {
		switch (event.getCode()) {
			case F:
/*				if (event.isControlDown()) {
					String namePattern = namePatternField.getText();
					if (!namePattern.isEmpty()) {
						namePattern = "(?i).*" + namePattern + ".*";
						imageCanvas.loadNextMatch(namePattern);
					}

*/				break;
			case ESCAPE:
				Platform.exit();
				break;
			case RIGHT:
				imageCanvas.loadNextImage();
				break;
			case LEFT:
				imageCanvas.loadPreviousImage();
				break;
		}
	}

	@FXML
	public void handleMousePressedEvent(MouseEvent event) {
		offsetX = event.getX();
		offsetY = event.getY();
	}

	@FXML
	public void handleMouseReleasedEvent() {
		offsetX = null;
		offsetX = null;
	}

	@FXML
	public void handleMouseDraggedEvent(MouseEvent event) {
		imageCanvas.setLayoutX(event.getX());
		imageCanvas.setLayoutY(event.getY());
//		imageCanvas.moveImage(event.getX(), event.getY());
//		imageCanvas.drawImage();
//		imageCanvas.moveImage(event.getX() - offsetX, event.getY() - offsetY);
//		offsetX = event.getX();
//		offsetY = event.getY();
	}
}
