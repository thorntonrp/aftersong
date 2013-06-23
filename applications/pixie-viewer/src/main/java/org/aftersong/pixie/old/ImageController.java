/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.old;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 *
 * @author Robert P. Thornton
 */
class ImageController {

	private Stage primaryStage;

	private ImageCanvas imageCanvas;

	public void initialize() {/*
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
				primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						imageCanvas.loadCurrentImage();
					}
				});
				imageCanvas.loadNextImage();
			}
		});
	}
}
