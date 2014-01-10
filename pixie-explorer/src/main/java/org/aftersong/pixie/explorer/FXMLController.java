package org.aftersong.pixie.explorer;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class FXMLController implements Initializable {

	@FXML
	private Pane parent;

	@FXML
	private ImageView imageView;

	private final AtomicInteger index = new AtomicInteger(-1);

	private final List<File> list = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		imageView.onScrollProperty().set(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent t) {
				if (t.getDeltaY() > 0) {
					loadImage(getNext());
				} else {
					loadImage(getPrevious());
				}
			}
		});

		final File pictures = new File(System.getProperty("user.home"), "Pictures");
		File folder = pictures;
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().matches("(?i).*\\.(jpg|png|bmp|gif)");
			}
		});
		list.addAll(Arrays.asList(files));
		loadImage(getNext());
	}

	private void loadImage(File imageFile) {
		try {
			double margin = 25;

			double maxWidth = Math.max(parent.getPrefWidth(), parent.getWidth()) - margin * 2;
			double maxHeight = Math.max(parent.getPrefHeight(), parent.getHeight()) - margin * 2;

			// Load the image with the supplied maximum width and maximum height
			Image image = new Image(imageFile.toURI().toURL().toExternalForm(), maxWidth, maxHeight, true, true, false);

			double width = image.getWidth();
			double height = image.getHeight();
			double x = margin + (maxWidth - width) / 2;
			double y = margin + (maxHeight - height) / 2;

			// Set view size to image size
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);

			imageView.setImage(image);
			imageView.relocate(x, y); // center view in parent

		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.toString(), ex);
		}
	}

	private File getPrevious() {
		if (index.get() == 0) {
			index.set(list.size());
		}
		File previous = list.get(index.decrementAndGet());
		return previous;
	}

	private File getNext() {
		if (index.get() == list.size() - 1) {
			index.set(-1);
		}
		File next = list.get(index.incrementAndGet());
		return next;
	}
}
