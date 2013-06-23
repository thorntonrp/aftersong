/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Robert P. Thornton
 */
public class SlidingBorderPane extends AnchorPane {

	private static final double MARGIN = 15;

	private ObjectProperty<Node> left;
	private ObjectProperty<Node> right;
	private ObjectProperty<Node> top;
	private ObjectProperty<Node> bottom;
	private ObjectProperty<Node> center;

	private VBox leftPane;
	private VBox rightPane;
	private HBox topPane;
	private HBox bottomPane;
	private Pane centerPane;

	private final MouseEnteredHandler mouseEnteredHandler;
	private final MouseExitedHandler mouseExitedHandler;

	public SlidingBorderPane() {
		mouseEnteredHandler = new MouseEnteredHandler();
		mouseExitedHandler = new MouseExitedHandler();

		leftPane   = new VBox(10);
		rightPane  = new VBox(10);
		topPane    = new HBox(10);
		bottomPane = new HBox(10);
		centerPane = new Pane();

		add(leftPane,    true,   0d,   0d,   0d, null);
		add(topPane,     true,   0d,   0d, null,   0d);
		add(rightPane,   true,   0d, null,   0d,   0d);
		add(bottomPane,  true, null,   0d,   0d,   0d);
		add(centerPane, false,   0d,   0d,   0d,   0d);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				leftPane.setTranslateX(-leftPane.getWidth() + MARGIN);
				rightPane.setTranslateX(rightPane.getWidth() - MARGIN);
				topPane.setTranslateY(-topPane.getHeight() + MARGIN);
				bottomPane.setTranslateY(topPane.getHeight() - MARGIN);
				Transforms.centerInParent(center.get());
				makeTransparent(leftPane, rightPane, topPane, bottomPane);
			}
		});
	}

	public ObjectProperty<Node> leftProperty() {
		if (left == null) {
			left = createBorderNodeProperty("left");
		}
		return left;
	}

	public ObjectProperty<Node> rightProperty() {
		if (right == null) {
			right = createBorderNodeProperty("right");
		}
		return right;
	}

	public ObjectProperty<Node> topProperty() {
		if (top == null) {
			top = createBorderNodeProperty("top");
		}
		return top;
	}

	public ObjectProperty<Node> bottomProperty() {
		if (bottom == null) {
			bottom = createBorderNodeProperty("bottom");
		}
		return bottom;
	}

	public ObjectProperty<Node> centerProperty() {
		if (center == null) {
			center = createBorderNodeProperty("center");
		}
		return center;
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

	private void makeTransparent(Node... nodes) {
		for (Node node : nodes) {
			node.setStyle("-fx-background-color: transparent;");
			node.toFront();
		}
	}

	private void makeOpaque(Node... nodes) {
		for (Node node : nodes) {
			node.setStyle("-fx-background-color: #EFEFEFFF;");
			node.toFront();
		}
	}

	private void add(Node node, boolean sliding, Double top, Double left, Double bottom, Double right) {
		getChildren().add(node);
		if (sliding) {
			mouseEnteredHandler.appendTo(node.onMouseEnteredProperty());
			mouseExitedHandler.appendTo(node.onMouseExitedProperty());
		}
		if (top != null) {
			AnchorPane.setTopAnchor(node, top);
		}
		if (left != null) {
			AnchorPane.setLeftAnchor(node, left);
		}
		if (bottom != null) {
			AnchorPane.setBottomAnchor(node, bottom);
		}
		if (right != null) {
			AnchorPane.setRightAnchor(node, right);
		}
	}

	private class MouseExitedHandler extends ChainableEventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			final Node node = (Node) event.getTarget();

			TranslateTransition hideTransition = new TranslateTransition();
			hideTransition.setNode(node);
			hideTransition.setDuration(new Duration(200));
			hideTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					makeTransparent(node);
				}
			});

			double width = node.getBoundsInLocal().getWidth();
			double height = node.getBoundsInLocal().getHeight();

			if (node == leftPane) {
				hideTransition.setToX(-width + MARGIN);
				hideTransition.setToY(0);
			} else if (node == rightPane) {
				hideTransition.setToX(width - MARGIN);
				hideTransition.setToY(0);
			} else if (node == topPane) {
				hideTransition.setToX(0);
				hideTransition.setToY(-height + MARGIN);
			} else if (node == bottomPane) {
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
			showTransition.setDuration(new Duration(200));
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

	private ObjectProperty<Node> createBorderNodeProperty(final String position) {
		return new SimpleObjectProperty<Node>(this, position) {

			@Override
			public void set(Node node) {
				switch (position) {
					case "left":
						leftPane.getChildren().clear();
						leftPane.getChildren().add(node);
						VBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
						VBox.setVgrow(node, Priority.ALWAYS);
						break;
					case "top":
						topPane.getChildren().clear();
						topPane.getChildren().add(node);
						HBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
						HBox.setHgrow(node, Priority.ALWAYS);
						break;
					case "right":
						rightPane.getChildren().clear();
						rightPane.getChildren().add(node);
						VBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
						VBox.setVgrow(node, Priority.ALWAYS);
						break;
					case "bottom":
						bottomPane.getChildren().clear();
						bottomPane.getChildren().add(node);
						HBox.setMargin(node, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
						HBox.setHgrow(node, Priority.ALWAYS);
						break;
					case "center":
						centerPane.getChildren().clear();
						centerPane.getChildren().add(node);
						break;
				}
				super.set(node);
			}
		};
	}
}
