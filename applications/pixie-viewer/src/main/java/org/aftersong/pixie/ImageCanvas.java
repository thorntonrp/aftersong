/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import org.aftersong.logging.Log;
import org.aftersong.logging.Logger;
import org.aftersong.pixie.events.ImageChangedEvent;
import org.aftersong.pixie.image.ImageResource;
import org.aftersong.pixie.image.ImageResourceMetaData;
import org.aftersong.pixie.image.ImageResourceMetaDataRepository;
import org.aftersong.pixie.image.ImageResourceRepository;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageCanvas extends Canvas {

	private static final Logger LOG = Log.getLogger();

	private static final double SCALE_FACTOR = 1.075;

	private static final double ROTATION_UNITS = 1;

	private ExecutorService executor;

	private Image image;

	private ImageCanvasModel model;
	private ImageResourceMetaDataRepository metaDataRepository;

	public ImageCanvas() {
	}

	public void setImageResourceMetaDataRepository(ImageResourceMetaDataRepository metaDataRepository) {
		this.metaDataRepository = metaDataRepository;
	}

	public void setModel(ImageCanvasModel model) {
		this.model = model;
	}

	public void setExecutorService(ExecutorService executor) {
		this.executor = executor;
	}

	//-- Image Loading -------------------------------------------------------//

	public void refreshImage() {
		queueLoadImageTask(model.current());
	}

	public void loadNextMatch(String namePattern) {
		queueLoadImageTask(model.nextMatch(namePattern));
	}

	public void loadNextImage() {
		queueLoadImageTask(model.next());
	}

	public void loadPreviousImage() {
		queueLoadImageTask(model.previous());
	}

	public void loadNextFolderImage() {
		queueLoadImageTask(model.nextFolder());
	}

	public void loadPreviousFolderImage() {
		queueLoadImageTask(model.previousFolder());
	}

	//-- Image Manipulation --------------------------------------------------//

	public void scaleImageUp() {
		ImageResource imageResource = model.get();
		ImageResourceMetaData metaData = getMetaData(imageResource);
		double newScale = metaData.getScale() * SCALE_FACTOR;
		if (newScale < 20) {
			scaleImage(imageResource, newScale);
		}
	}

	public void scaleImageDown() {
		ImageResource imageResource = model.get();
		ImageResourceMetaData metaData = getMetaData(imageResource);
		double newScale = metaData.getScale() / SCALE_FACTOR;
		if (newScale > 0.05) {
			scaleImage(imageResource, newScale);
		}
	}

	public void rotateLeft() {
		ImageResourceMetaData metaData = getMetaData();
		double rotation = metaData.getRotationAngle() - ROTATION_UNITS;
		metaData.setRotationAngle(rotation);
		metaData.setManuallyEdited(true);
		setRotate(rotation);
	}

	public void rotateRight() {
		ImageResourceMetaData metaData = getMetaData();
		double rotation = metaData.getRotationAngle() + ROTATION_UNITS;
		metaData.setRotationAngle(rotation);
		metaData.setManuallyEdited(true);
		setRotate(rotation);
	}

	public void moveImage(double x, double y) {
		getMetaData().setOffsetX(x);
		getMetaData().setOffsetY(y);
//		setTranslateX(x);
//		setTranslateY(y);
//		relocate(x, y);
//		getParent().layout();
	}

	//-- Private Operations --------------------------------------------------//

	private ImageResourceMetaData getMetaData() {
		return getMetaData(model.get());
	}

	private ImageResourceMetaData getMetaData(ImageResource imageResource) {
		if (imageResource.getMetaData() == null) {
			imageResource.setMetaData(metaDataRepository.getImageResourceMetaData(imageResource.getImageUri()));
		}
		return imageResource.getMetaData();
	}

	private void scaleImage(ImageResource imageResource, double newScale) {
		imageResource.getMetaData().setScale(newScale);
		imageResource.getMetaData().setManuallyEdited(true);
		setScaleX(newScale);
		setScaleY(newScale);
		fireEvent(new ImageChangedEvent(this, this, ImageChangedEvent.IMAGE_CHANGED, imageResource));
	}

	private void queueLoadImageTask(final int newIndex) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					loadImage(newIndex);
				} catch (Exception ex) {
					LOG.severe(ex);
				}
			}
		});
	}

	// Invoked concurrently *outside* the JavaFX Event Queue
	private void loadImage(final int newIndex) {
		ImageResource imageResource = model.get(newIndex);
		ImageResourceMetaData imageResourceMetaData = getMetaData(imageResource);

		final Image newImage;
		LOG.info("Loading image #{0} {1}", newIndex, imageResource.getImageUri());
		try {
			newImage = new Image(imageResource.getImageUri(), false);
		} catch (Throwable e) {
			LOG.severe(e);
			return;
		}
		Bounds parentBounds = getParent().getBoundsInLocal();
		Rect parentRect = new Rect(parentBounds.getWidth(), parentBounds.getHeight());
		Rect canvasRect = new Rect(newImage.getWidth(), newImage.getHeight());
		if (imageResourceMetaData.isManuallyEdited()) {

		} else {
			while (!parentRect.contains(canvasRect)) {
				canvasRect.setScale(canvasRect.getScale() / SCALE_FACTOR);
			}
			imageResourceMetaData.setScale(canvasRect.getScale());
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateImage(newIndex, newImage);
			}
		});
	}

	// Invoked on the JavaFX Event Queue
	private void updateImage(int newIndex, Image newImage) {
		ImageResource imageResource = model.get(newIndex);
		image = newImage;
//		relocate(imageResource.getMetaData().getOffsetX(),
//				 imageResource.getMetaData().getOffsetX());
		setWidth(newImage.getWidth());
		setHeight(newImage.getHeight());
//		setScaleX(imageResource.getMetaData().getScale());
//		setScaleY(imageResource.getMetaData().getScale());
//		setRotate(imageResource.getMetaData().getRotationAngle());
		drawImage();
		fireEvent(new ImageChangedEvent(this, this, ImageChangedEvent.IMAGE_CHANGED, imageResource));
	}

	public void drawImage() {
		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), 0, 0, getWidth(), getHeight());
	}

	private class Rect {

		private final double originalWidth;
		private final double originalHeight;

		private double scale = 1.0;

		Rect(double width, double height) {
			this.originalWidth = width;
			this.originalHeight = height;
		}

		public double getWidth() {
			return originalWidth * scale;
		}

		public double getHeight() {
			return originalHeight * scale;
		}

		public double getScale() {
			return scale;
		}

		public void setScale(double scale) {
			this.scale = scale;
		}

		public boolean contains(Rect rect) {
			return this.getWidth() >= rect.getWidth() &&
				   this.getHeight() >= rect.getHeight();
		}
	}
}
