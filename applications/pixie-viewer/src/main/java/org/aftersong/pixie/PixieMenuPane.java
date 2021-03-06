/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author Robert P. Thornton
 */
public class PixieMenuPane extends Pane {

	public PixieMenuPane() {
		getChildren().add(createExampleTextLabel("Pixie Application Pane"));
	}

	private Label createExampleTextLabel(String text) {
		Label label = new Label(text);
		label.setPrefSize(100, 100);
		label.setAlignment(Pos.CENTER);
		return label;
	}
}
