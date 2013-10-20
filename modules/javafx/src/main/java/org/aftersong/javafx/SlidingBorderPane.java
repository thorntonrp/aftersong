/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Robert P. Thornton
 */
public class SlidingBorderPane extends AnchorPane {

	private static final double MARGIN = 15;

	private ObjectProperty<Node> leftProperty;
	private ObjectProperty<Node> rightProperty;
	private ObjectProperty<Node> topProperty;
	private ObjectProperty<Node> bottomProperty;
	private ObjectProperty<Node> centerProperty;

	private ObjectProperty<Image> backgroundImageProperty;

	private SlidingPane leftSlidingPane;
	private SlidingPane rightSlidingPane;
	private SlidingPane topSlidingPane;
	private SlidingPane bottomSlidingPane;

	private Pane centerPane;
	private ImageView backgroundImageView;

	private final MouseEnteredHandler mouseEnteredHandler;
	private final MouseExitedHandler mouseExitedHandler;
	private final WidthChangeHandler widthChangeHandler;
	private final HeightChangeHandler heightChangeHandler;

	public SlidingBorderPane() {
		mouseEnteredHandler = new MouseEnteredHandler();
		mouseExitedHandler  = new MouseExitedHandler();
		widthChangeHandler  = new WidthChangeHandler();
		heightChangeHandler = new HeightChangeHandler();

		leftSlidingPane   = new SlidingPane(Position.LEFT);
		rightSlidingPane  = new SlidingPane(Position.RIGHT);
		topSlidingPane    = new SlidingPane(Position.TOP);
		bottomSlidingPane = new SlidingPane(Position.BOTTOM);

		addSlidingPanes(topSlidingPane, rightSlidingPane, bottomSlidingPane, leftSlidingPane);

		centerPane = new StackPane();
		getChildren().add(centerPane);
		layout(centerPane, 0d, 0d, 0d, 0d);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				leftSlidingPane.setTranslateX(-leftSlidingPane.getWidth() + MARGIN);
				rightSlidingPane.setTranslateX(rightSlidingPane.getWidth() - MARGIN);
				topSlidingPane.setTranslateY(-topSlidingPane.getHeight() + MARGIN);
				bottomSlidingPane.setTranslateY(topSlidingPane.getHeight() - MARGIN);
				Node centerNode = centerProperty().get();
				if (centerNode != null) {
					Transforms.centerInParent(centerNode);
				}
				makeTransparent(leftSlidingPane, rightSlidingPane, topSlidingPane, bottomSlidingPane);
			}
		});
	}

	public ObjectProperty<Node> leftProperty() {
		if (leftProperty == null) {
			leftProperty = createBorderNodeProperty(Position.LEFT);
		}
		return leftProperty;
	}

	public ObjectProperty<Node> rightProperty() {
		if (rightProperty == null) {
			rightProperty = createBorderNodeProperty(Position.RIGHT);
		}
		return rightProperty;
	}

	public ObjectProperty<Node> topProperty() {
		if (topProperty == null) {
			topProperty = createBorderNodeProperty(Position.TOP);
		}
		return topProperty;
	}

	public ObjectProperty<Node> bottomProperty() {
		if (bottomProperty == null) {
			bottomProperty = createBorderNodeProperty(Position.BOTTOM);
		}
		return bottomProperty;
	}

	public ObjectProperty<Node> centerProperty() {
		if (centerProperty == null) {
			centerProperty = createBorderNodeProperty(Position.CENTER);
		}
		return centerProperty;
	}

	public ObjectProperty<Image> backgroundImageProperty() {
		if (backgroundImageProperty == null) {
			backgroundImageProperty = new SimpleObjectProperty<Image>(this, "backgroundImage") {

				@Override
				public void set(Image image) {
					if (backgroundImageView != null) {
						getChildren().remove(backgroundImageView);
					}
					backgroundImageView = new ImageView(image);
					widthProperty().addListener(widthChangeHandler);
					heightProperty().addListener(heightChangeHandler);
					getChildren().add(backgroundImageView);
					layout(backgroundImageView, 0d, 0d, 0d, 0d);
					backgroundImageView.toBack();
					super.set(image);
				}
			};
		}
		return backgroundImageProperty;
	}

	public Node getLeft() {
		return leftProperty().get();
	}

	public void setLeft(Node node) {
		leftProperty().set(node);
	}

	public Node getRight() {
		return rightProperty().get();
	}

	public void setRight(Node node) {
		rightProperty().set(node);
	}

	public Node getTop() {
		return topProperty().get();
	}

	public void setTop(Node node) {
		topProperty().set(node);
	}

	public Node getBottom() {
		return bottomProperty().get();
	}

	public void setBottom(Node node) {
		bottomProperty().set(node);
	}

	public Node getCenter() {
		return centerProperty().get();
	}

	public void setCenter(Node node) {
		centerProperty().set(node);
	}

	public Image getBackgroundImage() {
		return backgroundImageProperty().get();
	}

	public void setBackgroundImage(Image image) {
		backgroundImageProperty().set(image);
	}

	private void makeTransparent(Node... nodes) {
		for (Node node : nodes) {
			node.getStyleClass().add("transparent");
			node.toFront();
		}
	}

	private void makeOpaque(Node... nodes) {
		for (Node node : nodes) {
			node.getStyleClass().remove("transparent");
			node.toFront();
		}
	}

	private static void layout(Node node, Double top, Double right, Double bottom, Double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
	}

	private void addSlidingPanes(Node... nodes) {
		for (Node node : nodes) {
			getChildren().add(node);
			mouseEnteredHandler.appendTo(node.onMouseEnteredProperty());
			mouseExitedHandler.appendTo(node.onMouseExitedProperty());
		}
	}

	private ObjectProperty<Node> createBorderNodeProperty(final Position position) {
		return new SimpleObjectProperty<Node>(this, position.name().toLowerCase()) {

			@Override
			public void set(Node node) {
				switch (position) {
					case LEFT:
						leftSlidingPane.getContentPane().getChildren().clear();
						if (node != null) {
							leftSlidingPane.getContentPane().getChildren().add(node);
							VBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
							VBox.setVgrow(node, Priority.ALWAYS);
						}
						break;
					case TOP:
						topSlidingPane.getContentPane().getChildren().clear();
						if (node != null) {
							topSlidingPane.getContentPane().getChildren().add(node);
							HBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
							HBox.setHgrow(node, Priority.ALWAYS);
						}
						break;
					case RIGHT:
						rightSlidingPane.getContentPane().getChildren().clear();
						if (node != null) {
							rightSlidingPane.getContentPane().getChildren().add(node);
							VBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
							VBox.setVgrow(node, Priority.ALWAYS);
						}
						break;
					case BOTTOM:
						bottomSlidingPane.getContentPane().getChildren().clear();
						if (node != null) {
							bottomSlidingPane.getContentPane().getChildren().add(node);
							HBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
							HBox.setHgrow(node, Priority.ALWAYS);
						}
						break;
					case CENTER:
						centerPane.getChildren().clear();
						if (node != null) {
							centerPane.getChildren().add(node);
						}
						break;
				}
				super.set(node);
			}
		};
	}

	private class WidthChangeHandler implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			backgroundImageView.setFitWidth(newValue.doubleValue());
		}
	}

	private class HeightChangeHandler implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			backgroundImageView.setFitHeight(newValue.doubleValue());
		}
	}

	private class MouseExitedHandler extends ChainableEventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			final Node node = (Node) event.getTarget();

			TranslateTransition hideTransition = new TranslateTransition();
			hideTransition.setNode(node);
			hideTransition.setDuration(new Duration(100));
			hideTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					makeTransparent(node);
				}
			});

			double width = node.getBoundsInLocal().getWidth();
			double height = node.getBoundsInLocal().getHeight();

			if (node == leftSlidingPane) {
				hideTransition.setToX(-width + MARGIN);
				hideTransition.setToY(0);
			} else if (node == rightSlidingPane) {
				hideTransition.setToX(width - MARGIN);
				hideTransition.setToY(0);
			} else if (node == topSlidingPane) {
				hideTransition.setToX(0);
				hideTransition.setToY(-height + MARGIN);
			} else if (node == bottomSlidingPane) {
				hideTransition.setToX(0);
				hideTransition.setToY(height - MARGIN);
			}

			hideTransition.play();
		}
	}

	private class MouseEnteredHandler extends ChainableEventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			final Node node = (Node) event.getTarget();

			makeOpaque(node);

			TranslateTransition showTransition = new TranslateTransition();
			showTransition.setNode(node);
			showTransition.setDuration(new Duration(100));
			showTransition.setToX(0);
			showTransition.setToY(0);
			showTransition.play();
			showTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					makeOpaque(node);
				}
			});
		}
	}
}
