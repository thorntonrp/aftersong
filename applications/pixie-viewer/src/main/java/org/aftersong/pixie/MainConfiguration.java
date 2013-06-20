/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import javax.inject.Inject;
import javax.inject.Provider;

import org.aftersong.pixie.Constants;
import org.aftersong.pixie.ImageController;
import org.aftersong.pixie.Main;
import org.aftersong.pixie.image.DefaultImageResourceMetaDataRepository;
import org.aftersong.pixie.image.DefaultImageResourceRepository;
import org.aftersong.pixie.image.DefaultImageResourceScanner;
import org.aftersong.pixie.image.ImageResourceMetaDataRepository;
import org.aftersong.pixie.image.ImageResourceRepository;
import org.aftersong.pixie.image.ImageResourceScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author Robert P. Thornton
 */
@Configuration
@ComponentScan(basePackageClasses = Main.class)
public class MainConfiguration implements Constants {

	@Autowired
	private ResourceLoader resourceLoader;

	@Inject
	private Provider<ImageController> imageController;

	@Lazy @Bean(destroyMethod = "shutdownNow")
	ExecutorService getExecutor() {
		return Executors.newFixedThreadPool(4);
	}

	@Lazy @Bean
	MessageSource getMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(RESOURCE_PACKAGE + ".message");
		return messageSource;
	}

	@Lazy @Bean
	Stage getInitializedStage(Stage primaryStage, Scene primaryScene) {
		Rectangle2D rect = Screen.getPrimary().getVisualBounds();
		primaryStage.setScene(primaryScene);
		primaryStage.getIcons().addAll(getWindowIcons());
		primaryStage.setTitle(TITLE);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setWidth(rect.getWidth());
		primaryStage.setHeight(rect.getHeight());
//		primaryStage.setFullScreen(true);
		return primaryStage;
	}

	@Lazy @Bean
	Scene getInitializedScene(Parent rootPane) {
		return new Scene(rootPane);
	}

	@Lazy @Bean(name = "rootPane")
	Parent getRootPane() throws IOException {
		ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE + ".messages");
		Resource fxmlResource = resourceLoader.getResource(RESOURCE_FOLDER + "/Main.fxml");
		return FXMLLoader.load(
				fxmlResource.getURL(), resources,
				new JavaFXBuilderFactory(),
				new Callback<Class<?>, Object>() {
					@Override
					public Object call(Class<?> param) {
						return imageController.get();
					}
				}, Charset.forName("UTF-8"));
	}

	@Lazy @Bean(name = "imageResourceScanner")
	ImageResourceScanner getImageResourceScanner() {
		DefaultImageResourceScanner scanner = new DefaultImageResourceScanner();
		scanner.setFileExtensions(".png", ".jpg", ".bmp", ".gif", ".jpeg");
		return scanner;
	}

	@Lazy @Bean(name = "imageResourceRepository")
	ImageResourceRepository getImageResourceRepository(ImageResourceScanner imageResourceScanner) {
		DefaultImageResourceRepository repository = new DefaultImageResourceRepository();
		repository.setImageResourceScanner(imageResourceScanner);
		return repository;
	}

	@Lazy @Bean(name = "imageResourceMetaDataRepository")
	ImageResourceMetaDataRepository getImageResourceMetaDataRepository() {
		return new DefaultImageResourceMetaDataRepository();
	}

	//-- Private Implementation ----------------------------------------------//

	private List<Image> getWindowIcons() {
		List<Image> iconImages = Arrays.asList(
				new Image(Main.class.getResource("icon-16.png").toString()),
				new Image(Main.class.getResource("icon-32.png").toString()),
				new Image(Main.class.getResource("icon-48.png").toString()));
		return iconImages;
	}
}
