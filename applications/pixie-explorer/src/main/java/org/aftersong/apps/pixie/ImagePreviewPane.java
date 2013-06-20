/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Robert P. Thornton
 */
public class ImagePreviewPane extends FlowPane {

	private static final Logger LOG = Logger.getLogger(ImagePreviewPane.class.getName());

	public ImagePreviewPane(List<Path> previewImages) {
		setStyle("-fx-background-color #404040");
		loadImages(previewImages);
	}

	private void loadImages(List<Path> previewImages) {
		List<Node> viewList = new ArrayList<>();
		for (Path imagePath: previewImages) {
			try {
				ImagePreviewThumbnail imageBox = new ImagePreviewThumbnail(imagePath);
				viewList.add(imageBox);
			} catch (MalformedURLException ex) {
				LOG.log(Level.SEVERE, null, ex);
			}
		}

		getChildren().addAll(viewList);
	}
}
