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
public class PixieImagePropertiesPane extends Pane {

	public PixieImagePropertiesPane() {
		getChildren().add(createExampleTextLabel("Pixie Image Operations Pane"));
	}

	private Label createExampleTextLabel(String text) {
		Label label = new Label(text);
		label.setPrefSize(100, 100);
		label.setAlignment(Pos.CENTER);
		return label;
	}
}
