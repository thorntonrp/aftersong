/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.modules.swing;

import java.awt.Component;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.aftersong.modules.logging.Log;

/**
 *
 * @author Robert Thornton
 */
public class LookAndFeel {

	private static final Log LOG = Log.getLogger();

	public static LookAndFeelInfo chooseLookAndFeel(Component parent) {
		if (EventQueue.isDispatchThread()) {
			Decorator<?>[] options = decorate(UIManager.getInstalledLookAndFeels());
			String defaultlValue = UIManager.getSystemLookAndFeelClassName();
			Wrapper<?> initialValue = new Wrapper<>(getLookAndFeel(defaultlValue));
			Decorator<?> value = (Decorator<?>) JOptionPane.showInputDialog(parent,
					"Choose Look and Feel", "Look and Feel",
					JOptionPane.PLAIN_MESSAGE, null, options, initialValue);
			return value == null ? null : (LookAndFeelInfo) value.getValue();
		} else {
			return invokeChooseLookAndFeel(parent);
		}
	}

	private static LookAndFeelInfo invokeChooseLookAndFeel(final Component parent) {
		final Wrapper<LookAndFeelInfo> result = new Wrapper<>();
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					result.setValue(chooseLookAndFeel(parent));
				}
			});
		} catch (InvocationTargetException | InterruptedException ex) {
			LOG.warning(ex);
		}
		return result.getValue();
	}

	public static void applyLookAndFeel(String name) {
		LookAndFeelInfo lafInfo = getLookAndFeel(name);
		if (lafInfo != null) {
			applyLookAndFeel(lafInfo);
		} else {
			LOG.warning("No matching look and feel installed: {0}", name);
		}
	}

	public static void applyLookAndFeel(LookAndFeelInfo lafInfo) {
		applyLookAndFeelByClassName(lafInfo.getClassName());
	}

	public static void applySystemLookAndFeel() {
		applyLookAndFeelByClassName(UIManager.getSystemLookAndFeelClassName());
	}

	//-- Private Implementation ----------------------------------------------//

	private static LookAndFeelInfo getLookAndFeel(String name) {
		for (LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
			if (lookAndFeelInfo.getClassName().equals(name) || lookAndFeelInfo.getName().equalsIgnoreCase(name)) {
				return lookAndFeelInfo;
			}
		}
		return null;
	}

	private static void applyLookAndFeelByClassName(String className) {
		try {
			UIManager.setLookAndFeel(className);
			LOG.info("Applied look and feel: {0}", UIManager.getLookAndFeel().getName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			LOG.warning(ex);
		}
	}

	private static Decorator<?>[] decorate(LookAndFeelInfo[] values) {
		Decorator<?>[] results = new Decorator<?>[values.length];
		for (int i = 0; i < results.length; i++) {
			final LookAndFeelInfo laf = values[i];
			results[i] = new Decorator<LookAndFeelInfo>(values[i]) {
				@Override
				public String toString() {
					return laf.getName();
				}
			};
		}
		return results;
	}

	private LookAndFeel() {}

	private static class Wrapper<T> {

		private T value;

		Wrapper() {}

		Wrapper(T value) {
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			return value == null ? 0 : value.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof Wrapper) {
				Wrapper<?> other = (Wrapper<?>) obj;
				if (value == other.value) {
					return true;
				} else if (value == null || other.value == null) {
					return false;
				}
				return value.equals(other.value);
			}
			return false;
		}
	}

	private static abstract class Decorator<T> extends Wrapper<T> {

		Decorator(T value) {
			super(value);
		}

		@Override
		public abstract String toString();
	}
}
