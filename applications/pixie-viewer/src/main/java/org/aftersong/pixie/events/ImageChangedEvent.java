/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import org.aftersong.pixie.image.ImageResource;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageChangedEvent extends Event {

	private static final long serialVersionUID = 1L;

	public static final EventType<ImageChangedEvent> IMAGE_CHANGED =
			new EventType<>("imageChanged");

	private final ImageResource imageResource;

	public ImageChangedEvent(Object source, EventTarget target,
			EventType<? extends Event> eventType, ImageResource imageResource) {
		super(source, target, eventType);
		this.imageResource = imageResource;
	}

	public ImageResource getImageResource() {
		return imageResource;
	}
}
