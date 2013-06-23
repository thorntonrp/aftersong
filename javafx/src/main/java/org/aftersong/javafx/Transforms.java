/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

/**
 *
 * @author Robert P. Thornton
 */
public class Transforms {

	private Transforms() {}

	public static void enableDrag(Node node) {
		new MouseMotionHandler()
			.appendTo(node.onMousePressedProperty())
			.appendTo(node.onMouseReleasedProperty())
			.appendTo(node.onMouseDraggedProperty());
	}

	public static void enableZoom(Node node) {
		new ZoomHandler().appendTo(node.onZoomProperty());
		new ScrollHandler().appendTo(node.onScrollProperty());
	}

	public static void enableRotate(Node node) {
		new RotateHandler().appendTo(node.onRotateProperty());
		new ScrollRotateHandler().appendTo(node.onScrollProperty());
	}

	private static class MouseMotionHandler extends ChainableEventHandler<MouseEvent> {

		private Double cursorX;
		private Double cursorY;

		@Override
		public void handle(MouseEvent event) {
			Node node = (Node) event.getSource();
			if (MouseEvent.MOUSE_DRAGGED.equals(event.getEventType())) {
				double newX = event.getSceneX();
				double newY = event.getSceneY();
				double left = node.getLayoutX() + newX - cursorX;
				double top = node.getLayoutY() + newY - cursorY;
				cursorX = newX;
				cursorY = newY;
				node.setLayoutX(left);
				node.setLayoutY(top);
			} else if (MouseEvent.MOUSE_PRESSED.equals(event.getEventType())) {
				cursorX = event.getSceneX();
				cursorY = event.getSceneY();
			} else {
				cursorX = null;
				cursorY = null;
			}
		}
	}

	private static class ZoomHandler extends ChainableEventHandler<ZoomEvent> {

		@Override
		public void handle(ZoomEvent event) {
			Node node = (Node) event.getSource();
			double scale = node.getScaleX();
			double factor = event.getZoomFactor();
			node.setScaleX(scale * factor);
			node.setScaleY(scale * factor);
		}
	}

	private static class ScrollHandler extends ChainableEventHandler<ScrollEvent> {

		private static final double DEFAULT_SCALE_FACTOR = 1.075;

		@Override
		public void handle(ScrollEvent event) {
			if (event.isControlDown()) {
				Node node = (Node) event.getSource();
				double newScale;
				if (event.getDeltaY() > 0) {
					newScale = node.getScaleX() * DEFAULT_SCALE_FACTOR;
				} else {
					newScale = node.getScaleX() / DEFAULT_SCALE_FACTOR;
				}
				node.setScaleX(newScale);
				node.setScaleY(newScale);
			}
		}
	}

	private static class RotateHandler extends ChainableEventHandler<RotateEvent> {

		@Override
		public void handle(RotateEvent event) {
			Node node = (Node) event.getSource();
			double deltaAngle = event.getAngle();
			double currentAngle = node.getRotate();
			node.setRotate(currentAngle + deltaAngle);
		}
	}

	private static class ScrollRotateHandler extends ChainableEventHandler<ScrollEvent> {

		@Override
		public void handle(ScrollEvent event) {
			if (event.isAltDown()) {
				Node node = (Node) event.getSource();
				double currentAngle = node.getRotate();
				double deltaAngle;
				if (event.getDeltaY() > 0) {
					deltaAngle = -1;
				} else {
					deltaAngle = 1;
				}
				node.setRotate(currentAngle + deltaAngle);
			}
		}
	}
}
