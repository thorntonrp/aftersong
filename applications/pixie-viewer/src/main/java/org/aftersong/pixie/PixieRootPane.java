/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

import org.aftersong.javafx.SlidingBorderPane;

/**
 *
 * @author Robert Thornton
 */
public class PixieRootPane extends SlidingBorderPane {

	public PixieRootPane() {
		// Apply styling
		setStyle("-fx-background-color: #1A1A33");

		// Initialize anchored child nodes
		setLeft(new PixieFileExplorerPane());
		setRight(new PixieImagePropertiesPane());
		setTop(new PixieMenuPane());
		setBottom(new PixieImageOperationsPane());

		// Initialize image canvas
		getChildren().add(PixieCanvas.getInstance());

		// Add image file navigation behavior
		setOnScroll(new ImageNavigationScrollHandler());
		setOnKeyPressed(new ImageNavigationKeyPressedHandler());

		setFocusTraversable(true); // Allows keyboard input on root pane
	}

	//-- Private Operations --------------------------------------------------//

	private class ImageNavigationKeyPressedHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
				case F:/*
					if (event.isControlDown()) {
						String namePattern = namePatternField.getText();
						if (!namePattern.isEmpty()) {
							namePattern = "(?i).*" + namePattern + ".*";
							imageCanvas.loadNextMatch(namePattern);
						}
					}*/
					break;
				case ESCAPE:
					Platform.exit();
					break;
				case RIGHT:
					PixieCanvas.getInstance().loadNextImage();
					break;
				case LEFT:
					PixieCanvas.getInstance().loadPreviousImage();
					break;
			}
		}
	}

	private class ImageNavigationScrollHandler implements EventHandler<ScrollEvent> {

		@Override
		public void handle(ScrollEvent event) {
			PixieCanvas canvas = PixieCanvas.getInstance();
			if (event.isShiftDown()) {
				if (event.getDeltaY() > 0) {
					canvas.loadPreviousFolderImage();
				} else {
					canvas.loadNextFolderImage();
				}
			} else if (!event.isControlDown() && !event.isAltDown() && !event.isMetaDown() && !event.isShortcutDown()) {
				if (event.getDeltaY() > 0) {
					canvas.loadPreviousImage();
				} else {
					canvas.loadNextImage();
				}
			}
		}
	}
}
