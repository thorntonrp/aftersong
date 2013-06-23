/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author Robert P. Thornton
 */
public class EventHandlerChain implements EventHandler<Event> {

	private List<EventHandler<Event>> handlers = new ArrayList<>();

	@Override
	public void handle(Event event) {
		for (EventHandler<Event> nextHandler : handlers) {
			nextHandler.handle(event);
		}
	}

	public EventHandlerChain appendTo(EventHandler<Event> handler) {
		handlers.add(handler);
		return this;
	}

	public EventHandlerChain detachFrom(EventHandler<Event> handler) {
		handlers.remove(handler);
		return this;
	}
}
