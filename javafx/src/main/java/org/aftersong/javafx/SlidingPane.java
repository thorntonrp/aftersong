/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Robert P. Thornton
 */
class SlidingPane extends AnchorPane {

	private static final double SPACING = 10;

	private Pane contentPane;

	SlidingPane(Position position) {
		initContentPane(position);
	}

	Pane getContentPane() {
		return contentPane;
	}

	private void initContentPane(Position position) throws IllegalArgumentException {
		switch (position) {
			case LEFT:
				contentPane = new VBox(SPACING);
				layout(this, 0d, null, 0d, 0d);
				break;
			case RIGHT:
				contentPane = new VBox(SPACING);
				layout(this, 0d, 0d, 0d, null);
				break;
			case TOP:
				contentPane = new HBox(SPACING);
				layout(this, 0d, 0d, null, 0d);
				break;
			case BOTTOM:
				contentPane = new HBox(SPACING);
				layout(this, null, 0d, 0d, 0d);
				break;
			default:
				throw new IllegalArgumentException("Invalid position: " + position);
		}

		super.getChildren().add(contentPane);

		layout(contentPane, 0d, 0d, 0d, 0d);
	}

	private static void layout(Node node, Double top, Double right, Double bottom, Double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
	}
}
