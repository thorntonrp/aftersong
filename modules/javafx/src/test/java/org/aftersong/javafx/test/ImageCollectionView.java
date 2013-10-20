/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx.test;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import org.aftersong.javafx.SlidingBorderPane;
import org.aftersong.javafx.Transforms;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageCollectionView extends SlidingBorderPane {

	private static final double SCALE_FACTOR = 1.075;

	private final SizeChangeHandler sizeChangeHandler = new SizeChangeHandler();

	private final ImageView imageView;

	public ImageCollectionView() {
		setBackgroundImage(new Image(getClass().getResourceAsStream("background.png")));
		getStylesheets().add(getClass().getResource("TestView.css").toString());

		LabelBuilder<?> label = LabelBuilder.create()
				.prefWidth(160).prefHeight(120)
				.alignment(Pos.CENTER);

		setTop(label.text("TOP").build());
		setRight(label.text("RIGHT").build());
		setBottom(label.text("BOTTOM").build());
		setLeft(label.text("LEFT").build());

		imageView = new ImageView();
		Transforms.enableDrag(imageView);
		Transforms.enableZoom(imageView);
		Transforms.enableRotate(imageView);
		setCenter(imageView);

		widthProperty().addListener(sizeChangeHandler);
		heightProperty().addListener(sizeChangeHandler);
	}

	public void setImage(Image image) {
		double scale = calculateScale(image);
		imageView.setScaleX(scale);
		imageView.setScaleY(scale);
		imageView.setRotate(0.0);
		imageView.setImage(image);
		StackPane.setAlignment(imageView, Pos.CENTER);
	}

	//-- Private Implementation ----------------------------------------------//

	private double calculateScale(Image image) {
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		double containerWidth = getWidth();
		double containerHeight = getHeight();
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

	private class SizeChangeHandler implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			double scale = calculateScale(imageView.getImage());
			imageView.setScaleX(scale);
			imageView.setScaleY(scale);
		}
	}
}
