/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.swing;

import static org.testng.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.aftersong.core.Threads;
import org.testng.annotations.Test;

/**
 *
 * @author Robert P. Thornton
 */
public class LookAndFeelTest {

	private static final String NIMBUS = "Nimbus";

	@Test
	public void testApplySystemLookAndFeel() {
		LookAndFeel.applySystemLookAndFeel();
		assertEquals(UIManager.getLookAndFeel().getClass().getName(), UIManager.getSystemLookAndFeelClassName());
	}

	@Test
	public void testApplyLookAndFeel() {
		LookAndFeel.applyLookAndFeel(NIMBUS);
		assertEquals(UIManager.getLookAndFeel().getName(), NIMBUS);
	}

	@Test
	public void testApplyLookAndFeel_Invalid() {
		LookAndFeel.applyLookAndFeel("Foo");
		assertNotEquals(UIManager.getLookAndFeel().getName(), "Foo");
	}

	@Test
	public void testChooseLookAndFeel_onDispatchThread() throws Exception {
		class Result {
			LookAndFeelInfo value;
		}
		final Result result = new Result();
		new Thread(new Runnable() {
			@Override
			public void run() {
				result.value = LookAndFeel.chooseLookAndFeel(null);
			}
		}).start();

		Threads.sleep(2000);

		JDialog dialog = findComponent(Window.getWindows(), JDialog.class);
		assertNotNull(dialog);
		JOptionPane options = findComponent(dialog, JOptionPane.class);
		assertNotNull(options);
		options.setValue(options.getSelectionValues());
		List<JButton> buttons = findComponents(dialog, JButton.class);
		assertFalse(buttons.isEmpty());
		for (JButton button : buttons) {
			if ("OK".equals(button.getActionCommand())) {
				button.doClick();
			}
		}
		dialog.setVisible(false);
	}

	private static <C extends Component> C findComponent(Container container, Class<C> type) {
		return findComponent(container.getComponents(), type);
	}

	private static <C extends Component> C findComponent(Component[] components, Class<C> type) {
		C result = null;
		for (int i = 0; result == null && i < components.length; i++) {
			Component c = components[i];
			if (type.isInstance(c)) {
				result = type.cast(c);
			} else if (Container.class.isInstance(c)) {
				result = findComponent((Container) c, type);
			}
		}
		return result;
	}

	private static <C extends Component> List<C> findComponents(Container container, Class<C> type) {
		return findComponents(container.getComponents(), type);
	}

	private static <C extends Component> List<C> findComponents(Component[] components, Class<C> type) {
		List<C> results = new ArrayList<>();
		findComponents(components, type, results);
		return results;
	}

	private static <C extends Component> void findComponents(Container container, Class<C> type, List<C> results) {
		findComponents(container.getComponents(), type, results);
	}

	private static <C extends Component> void findComponents(Component[] components, Class<C> type, List<C> results) {
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (type.isInstance(c)) {
				results.add(type.cast(c));
			} else if (Container.class.isInstance(c)) {
				findComponents((Container) c, type, results);
			}
		}
	}
}
