/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.example;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;
import javax.inject.Singleton;
import javax.swing.JFrame;
import org.springframework.stereotype.Component;

/**
 *
 * @author Robert P. Thornton
 */
@Singleton
@Component
public class MainFrame extends JFrame {

	public MainFrame() {
		loadIconImages();
		setTitle("Spring Example");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
	}

	private void loadIconImages() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		List<Image> iconImages = Arrays.asList(
				toolkit.createImage(getClass().getResource("icon-16.png")),
				toolkit.createImage(getClass().getResource("icon-24.png")),
				toolkit.createImage(getClass().getResource("icon-32.png")),
				toolkit.createImage(getClass().getResource("icon-48.png")),
				toolkit.createImage(getClass().getResource("icon-64.png")),
				toolkit.createImage(getClass().getResource("icon-128.png")),
				toolkit.createImage(getClass().getResource("icon-256.png")));
		setIconImages(iconImages);
	}
}
