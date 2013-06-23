/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

import org.aftersong.collections.Lists;
import org.aftersong.javafx.SlidingBorderPane;
import org.aftersong.javafx.Transforms;

/**
 *
 * @author Robert Thornton
 */
public class PixieRootPane extends SlidingBorderPane {

	private ImageView imageView;

	private final List<Path> images;
	private int index;

	public PixieRootPane() {
		setStyle("-fx-background-color: #1A1A33");

		initLeftPaneChildren();
		initRightPaneChildren();
		initTopPaneChildren();
		initBottomPaneChildren();


//		setStyle("-fx-background-color: radial-gradient(focus-angle 90, focus-distance 100%, center 50% 50%, radius 50%, #CDCDFF,#1A1A33);");
//		setStyle("-fx-background-image: url('org/aftersong/pixie/background.png')");

		index = 0;
		images = Lists.newList(
				Paths.get(System.getProperty("user.home"), "Pictures", "Spuddy.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "001d4b1b-2dc0-440d-a20d-18703154cb7c_5.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "01c28269-19ee-41bb-ba9d-a54b7309af74_12.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "044f9e31-da10-498f-869a-b3f6be01400a_5.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "0c8013e4-0b98-4651-9d25-9e34704f3e4f_13.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "106c6b8d-7dc5-458a-a084-6b912080a8ae_5.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "124d8506-8a34-40e6-b99a-c6c63ef2fa2c_5.jpg"),
				Paths.get(System.getProperty("user.home"), "Pictures", "WallPapers", "150df948-244c-4682-909b-adc5cbba96a4_5.jpg")
			);

		setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				index++;
				if (index == images.size()) {
					index = 0;
				}
				imageView.setImage(new Image(images.get(index).toUri().toString()));
			}
		});

		initImageView();
	}

	private void initImageView() {
		imageView = new ImageView();
		getChildren().add(imageView);
		imageView.setImage(new Image(images.get(0).toUri().toString(), 1200.0, 800.0, true, false));
		Transforms.enableDrag(imageView);
		Transforms.enableZoom(imageView);
		Transforms.enableRotate(imageView);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				imageView.setLayoutX(getLeft().getParent().getLayoutBounds().getWidth() + 15);
				imageView.setLayoutY(getTop().getParent().getLayoutBounds().getHeight() + 15);
			}
		});
	}

	private void initLeftPaneChildren() {
		setLeft(createExampleTextLabel("LEFT"));
	}

	private void initRightPaneChildren() {
		setRight(createExampleTextLabel("RIGHT"));
	}

	private void initTopPaneChildren() {
		setTop(createExampleTextLabel("TOP"));
	}

	private void initBottomPaneChildren() {
		setBottom(createExampleTextLabel("BOTTOM"));
	}

	private Label createExampleTextLabel(String text) {
		Label label = new Label(text);
		label.setPrefSize(100, 100);
		label.setAlignment(Pos.CENTER);
		return label;
	}
}
