/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;

import org.aftersong.logging.Logger;

import static org.aftersong.logging.Logger.getLogger;

/**
 *
 * @author Robert P. Thornton
 */
class TestController {

	private static final Logger LOG = getLogger();

	private CircularIterator<String> model;
	private ImageCollectionView rootPane;
	private ExecutorService executor;

	TestController() {}

	void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	void setRootPane(ImageCollectionView rootPane) {
		this.rootPane = rootPane;
	}

	void initialize(Future<CircularIterator<String>> futureModel) {
		rootPane.setOnScroll(new ScrollNavigationHandler());
		try {
			model = futureModel.get();
			if (model.hasNext()) {
				rootPane.setImage(new Image(model.next()));
			}
		} catch (InterruptedException | ExecutionException ex) {
			LOG.severe(ex);
		}
	}

	//-- Private Operations --------------------------------------------------//

	private void loadImage(final String uri) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				final Image image = new Image(uri);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("Loaded " + uri);
						rootPane.setImage(image);
					}
				});
			}
		});
	}

	//-- Event Handling ------------------------------------------------------//

	private class ScrollNavigationHandler implements EventHandler<ScrollEvent> {

		@Override
		public void handle(ScrollEvent event) {
			if (!event.isAltDown() && !event.isControlDown()
				&& !event.isMetaDown() && !event.isShiftDown()
				&& !event.isShortcutDown()) {

				final String uri;
				if (event.getDeltaY() > 0) {
					uri = model.previous();
				} else {
					uri = model.next();
				}
				loadImage(uri);
			}
		}
	}
}
