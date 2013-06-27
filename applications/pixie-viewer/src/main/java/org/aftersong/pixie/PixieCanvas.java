/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.net.URLDecoder;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.aftersong.javafx.Transforms;
import org.aftersong.logging.Log;
import org.aftersong.logging.Logger;

/**
 *
 * @author Robert P. Thornton
 */
public class PixieCanvas extends ImageView {

	private static final Logger LOG = Log.getLogger();

	private static final double SCALE_FACTOR = 1.075;

	private static PixieCanvas instance;

	public static PixieCanvas getInstance() {
		if (instance == null) {
			instance = new PixieCanvas();
			Transforms.enableDrag(instance);
			Transforms.enableZoom(instance);
			Transforms.enableRotate(instance);
		}
		return instance;
	}

	private final PixieModel model;

	private PixieCanvas() {
		model = new PixieModel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (model.size() > 0) {
					loadNextImage();
				}
			}
		});
	}

	public void loadCurrentImage() {
		queueImage(model.current());
	}

	public void loadPreviousImage() {
		queueImage(model.previous());
	}

	public void loadNextImage() {
		queueImage(model.next());
	}

	public void loadPreviousFolderImage() {
		queueImage(model.previousFolder());
	}

	public void loadNextFolderImage() {
		queueImage(model.nextFolder());
	}

	private void queueImage(final int newIndex) {
		PixieApplication.submitTask(new Runnable() {
			@Override
			public void run() {
				loadImage(newIndex);
			}
		});
	}

	// Invoked concurrently *outside* the JavaFX Event Queue
	private void loadImage(final int index) {
		try {
			String uri = model.get(index).getImageUri();
			String baseUri = Paths.get(System.getProperty("user.home"), "Pictures").toUri().toString();
			String relativeUri = URLDecoder.decode(uri.substring(baseUri.length()), "UTF-8");
			LOG.info("Loading image: #{0,Number,#} {1}", index, relativeUri);
			final Image image = new Image(uri);

			Bounds parentBounds = getParent().getBoundsInLocal();
			BoundingRect parentRect = new BoundingRect(parentBounds.getWidth(), parentBounds.getHeight());
			final BoundingRect newBounds = new BoundingRect(image.getWidth(), image.getHeight());
			LOG.info("Parent bounds: ({0,Number,#}, {1,Number,#})", parentRect.getWidth(), parentRect.getHeight());
			LOG.info("Loaded image: #{0,Number,#} ({1,Number,#}, {2,Number,#})", index, newBounds.getWidth(), newBounds.getHeight());
			while (!parentRect.contains(newBounds)) {
				newBounds.setScale(newBounds.getScale() / SCALE_FACTOR);
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					updateImage(index, image, newBounds);
				}
			});
		} catch (Throwable thrown) {
			LOG.warning(thrown);
		}
	}

	// Invoked normally on the JavaFX Event Queue
	private void updateImage(int index, Image image, BoundingRect newBounds) {
		setImage(image);
		setScaleX(newBounds.getScale());
		setScaleY(newBounds.getScale());
		centerInParent(index, newBounds);
	}

	private void centerInParent(int index, BoundingRect newBounds) {
//		PixieRootPane root = (PixieRootPane) getParent();

//		double x = (root.getWidth() - newBounds.getWidth()) / 2;
//		double y = (root.getHeight() - newBounds.getHeight()) / 2;

//		setLayoutX(x);
//		setLayoutY(y);

//		LOG.info("Centered image: #{0,Number,#} ({1,Number,#}, {2,Number,#}, {3,Number,#}, {4,Number,#})", index, x, y, newBounds.getWidth(), newBounds.getHeight());
	}

	private static class BoundingRect {

		private final double originalWidth;
		private final double originalHeight;

		private double scale = 1.0;

		BoundingRect(double width, double height) {
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

		public boolean contains(BoundingRect rect) {
			return this.getWidth() >= rect.getWidth() &&
				   this.getHeight() >= rect.getHeight();
		}
	}
}
