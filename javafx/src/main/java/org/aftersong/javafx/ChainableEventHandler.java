/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @param <E>
 *
 * @author Robert P. Thornton
 */
public abstract class ChainableEventHandler<E extends Event> implements EventHandler<E> {

	@SuppressWarnings("unchecked")
	protected ChainableEventHandler<E> appendTo(ObjectProperty<EventHandler<? super E>> handlerProperty) {
		EventHandler<Event> current = (EventHandler<Event>) handlerProperty.get();
		if (current instanceof EventHandlerChain) {
			((EventHandlerChain) current).appendTo((EventHandler<Event>) this);
		} else if (current == null) {
			handlerProperty.set(this);
		} else {
			EventHandlerChain chain = new EventHandlerChain();
			chain.appendTo(current).appendTo((EventHandler<Event>) this);
			handlerProperty.set(chain);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	protected ChainableEventHandler<E> detachFrom(ObjectProperty<EventHandler<? super E>> handlerProperty) {
		EventHandler<Event> current = (EventHandler<Event>) handlerProperty.get();
		if (current == this) {
			handlerProperty.set(null);
		} else if (current instanceof EventHandlerChain) {
			((EventHandlerChain) current).detachFrom((EventHandler<Event>) this);
		}
		return this;
	}
}
