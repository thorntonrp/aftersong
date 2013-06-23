/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.old;

import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.aftersong.javafx.Transforms;
import org.aftersong.logging.Log;
import org.aftersong.logging.Logger;
import org.aftersong.pixie.PixieModel;
import org.aftersong.pixie.events.ImageChangedEvent;
import org.aftersong.pixie.image.ImageResource;
import org.aftersong.pixie.image.ImageResourceMetaData;
import org.aftersong.pixie.image.ImageResourceMetaDataRepository;

/**
 *
 * @author Robert P. Thornton
 */
class ImageCanvas extends ImageView {

	private static final Logger LOG = Log.getLogger();

	private static final double SCALE_FACTOR = 1.075;

	private static ImageCanvas instance;

	public static ImageCanvas getInstance() {
		if (instance == null) {
			instance = new ImageCanvas();
			Transforms.enableDrag(instance);
			Transforms.enableZoom(instance);
			Transforms.enableRotate(instance);
		}
		return instance;
	}

	private final PixieModel model;
	private ExecutorService executor;
	private ImageResourceMetaDataRepository metaDataRepository;

	private ImageCanvas() {
		model = new PixieModel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

			}
		});
	}

	//-- Image Loading -------------------------------------------------------//

	public void loadCurrentImage() {
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

	//-- Private Operations --------------------------------------------------//

	private ImageResourceMetaData getMetaData(ImageResource imageResource) {
		if (imageResource.getMetaData() == null) {
			imageResource.setMetaData(metaDataRepository.getImageResourceMetaData(imageResource.getImageUri()));
		}
		return imageResource.getMetaData();
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
		setImage(newImage);
		setFitWidth(newImage.getWidth());
		setFitHeight(newImage.getHeight());
		fireEvent(new ImageChangedEvent(this, this, ImageChangedEvent.IMAGE_CHANGED, imageResource));
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
