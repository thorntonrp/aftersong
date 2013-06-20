/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie;

import java.net.MalformedURLException;
import java.nio.file.Path;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Robert P. Thornton
 */
public class ImagePreviewThumbnail extends VBox {

	private static final int VERTICAL_SPACING = 5;
	private static final int IMAGE_SIZE = 256;

	private static final int PADDING_INSETS = 15;
	private static final int SHADOW_SIZE = 5;
	private static final double SHADOW_OPACITY = 0.5f;
	private static final String SHADOW_COLOR = "#000000";

	private static final Insets PADDING = new Insets(PADDING_INSETS);
	private static final DropShadow DROP_SHADOW = new DropShadow(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, Color.web(SHADOW_COLOR, SHADOW_OPACITY));
	
	public ImagePreviewThumbnail(Path imagePath) throws MalformedURLException {
		super(VERTICAL_SPACING);

		Image image = new Image(imagePath.toUri().toURL().toExternalForm(), IMAGE_SIZE, IMAGE_SIZE, true, true, true);
		ImageView view = new ImageView(image);
		
		view.setEffect(DROP_SHADOW);

		setAlignment(Pos.CENTER);
		setPadding(PADDING);
		setPrefWidth(IMAGE_SIZE);
		setPrefHeight(IMAGE_SIZE);

		getChildren().add(view);
		getChildren().add(LabelBuilder.create().text(imagePath.getFileName().toString()).textFill(Color.WHITE).build());

		setOpacity(1);
	}
}
