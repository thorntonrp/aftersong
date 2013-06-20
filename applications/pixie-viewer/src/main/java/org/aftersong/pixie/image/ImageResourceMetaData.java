/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Robert P. Thornton
 */
@XmlRootElement(name = "image")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageResourceMetaData {

	@XmlAttribute(name = "uri")
	private String imageUri;

	private double scale = 1.0;

	private double offsetX = 0.0;
	private double offsetY = 0.0;
	private double rotationAngle = 0.0;

	private boolean manuallyEdited = false;

	private Date firstLoaded;
	private Date lastEdited;
	
	private ImageResourceMetaData() {}

	ImageResourceMetaData(String imageUri) {
		this.imageUri = imageUri;
		this.firstLoaded = new Date();
	}

	public String getImageUri() {
		return imageUri;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public boolean isManuallyEdited() {
		if (manuallyEdited && lastEdited == null) {
			lastEdited = new Date();
		}
		return manuallyEdited;
	}

	public void setManuallyEdited(boolean manuallyScaled) {
		this.lastEdited = new Date();
		this.manuallyEdited = manuallyScaled;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	public double getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
}
