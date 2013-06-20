/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.apps.pixie;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;

import org.aftersong.apps.pixie.file.FileItem;
import org.aftersong.apps.pixie.file.FileItems;
import org.aftersong.modules.logging.Log;

/**
 *
 * @author Robert P. Thornton
 */
public class GalleryController {

	private static final Log LOG = Log.getLogger();

	private static final int THUMBNAIL_WIDTH = 64;
	private static final int THUMBNAIL_HEIGHT = 64;

	private static final String FOLDER = "folder.png";
//	@FXML
//	private URL location;

//	@FXML
//	private ResourceBundle resources;

	@FXML
	private Accordion imageFoldersAccordion;

	@FXML
	private ScrollPane imageScrollPane;

//	@FXML
//	private FlowPane imagePane;

	@FXML
	private ImageView imageView;

//	private final Object treeLock = new Object();

//	private ExecutorService executor;

	private Image folderImage;

	@FXML
	void initialize() {
//		executor = Executors.newSingleThreadExecutor();
		folderImage = new Image(getClass().getResource(FOLDER).toString(), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, true, false, true);
		File initialImageFolder = Paths.get(System.getProperty("user.home"), "Pictures").toFile();
		addNewImageFolder(initialImageFolder);
	}

	@FXML
	void handleAction(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof MenuItem) {
			String id = ((MenuItem) event.getSource()).getId();
			if (id != null) {
				switch (id) {
					case "File-AddNewFolder":
						DirectoryChooser chooser = new DirectoryChooser();
						addNewImageFolder(chooser.showDialog(Gallery.getGallery().getPrimaryStage()));
						break;
				}
			}
		}
	}

	public void addNewImageFolder(File directory) {
		String path = directory.getPath();
		TreeItem<FileItem> rootItem = new TreeItem<>(new FileItem(directory, true));
		rootItem.setExpanded(true);
		TreeView<FileItem> treeView = new TreeView<>(rootItem);
		populateFileTree(treeView, rootItem);
		treeView.setShowRoot(false);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<FileItem>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<FileItem>> observable, TreeItem<FileItem> oldValue, TreeItem<FileItem> newValue) {
				if (newValue != null && newValue.isLeaf()) {
					try {
						Image image = new Image(newValue.getValue().getURL().toString());
						double width = Math.min(image.getWidth(), imageScrollPane.getWidth());
						double height = Math.min(image.getHeight(), imageScrollPane.getHeight());
						imageView.setFitWidth(width);
						imageView.setFitHeight(height);
						imageView.setImage(image);
					} catch (MalformedURLException ex) {
						LOG.warning(ex.getMessage());
					}
				}
			}
		});
		TitledPane folderTitlePanel = new TitledPane(path, treeView);
		imageFoldersAccordion.getPanes().add(folderTitlePanel);
		imageFoldersAccordion.setExpandedPane(folderTitlePanel);
	}

	private void populateFileTree(TreeView<FileItem> treeView, TreeItem<FileItem> directoryTreeItem) {
		List<FileItem> directoryList = FileItems.list(directoryTreeItem.getValue());
		List<TreeItem<FileItem>> childTreeItems = new ArrayList<>();
		for (FileItem fileItem : directoryList) {
			try {
				ImageView thumbnailImageView;
				if (fileItem.isDirectory()) {
					thumbnailImageView = new ImageView(folderImage);
				} else {
					thumbnailImageView = new ImageView(new Image(fileItem.getURL().toString(), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, true, false, true));
				}
				FlowPane pane = new FlowPane();
				pane.setAlignment(Pos.CENTER);
				pane.getChildren().add(thumbnailImageView);
				pane.setPrefWidth(THUMBNAIL_WIDTH);
				TreeItem<FileItem> childTreeItem = new FileTreeItem(fileItem, pane);
				if (fileItem.isDirectory()) {
					addExpansionListener(treeView, childTreeItem);
				}
				childTreeItems.add(childTreeItem);
			} catch (MalformedURLException ex) {
				LOG.warning(ex.getMessage());
			}
		}
		if (!childTreeItems.isEmpty()) {
			directoryTreeItem.getChildren().addAll(childTreeItems);
			treeView.getSelectionModel().select(null);
		}
	}

	void addExpansionListener(TreeView<FileItem> treeView, TreeItem<FileItem> directoryTreeItem) {
		directoryTreeItem.expandedProperty().addListener(new TreeExpansionListener(treeView, directoryTreeItem));
	}

	private static class FileTreeItem extends TreeItem<FileItem> {

		public FileTreeItem(FileItem fileItem, Node node) {
			super(fileItem, node);
		}

		@Override
		public boolean isLeaf() {
			return !getValue().isDirectory();
		}
	}

	private class TreeExpansionListener implements ChangeListener<Boolean> {

		private final TreeView<FileItem> treeView;
		private final TreeItem<FileItem> directoryTreeItem;

		TreeExpansionListener(TreeView<FileItem> treeView, TreeItem<FileItem> directoryTreeItem) {
			this.treeView = treeView;
			this.directoryTreeItem = directoryTreeItem;
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (newValue && !newValue.equals(oldValue) && directoryTreeItem.getChildren().size() == 0) {
				directoryTreeItem.getChildren().clear();
				populateFileTree(treeView, directoryTreeItem);
			}
		}
	}
}
