/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Robert P. Thornton
 */
public class HelloFX extends Application {

	@Override
	public void start(Stage primaryStage) {
		List<Image> iconImages = Arrays.asList(
					new Image(getClass().getResourceAsStream("icon-16.png")),
					new Image(getClass().getResourceAsStream("icon-24.png")),
					new Image(getClass().getResourceAsStream("icon-32.png")),
					new Image(getClass().getResourceAsStream("icon-48.png")),
					new Image(getClass().getResourceAsStream("icon-64.png")),
					new Image(getClass().getResourceAsStream("icon-128.png")),
					new Image(getClass().getResourceAsStream("icon-256.png"))
				);
		primaryStage.getIcons().addAll(iconImages);
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World! (" + System.getProperty("java.version") + ")");
			}
		});

		ImageView iv = new ImageView(iconImages.get(iconImages.size() - 1));

		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: #A0A0C0");
		root.getChildren().add(iv);
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 256, 256);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
