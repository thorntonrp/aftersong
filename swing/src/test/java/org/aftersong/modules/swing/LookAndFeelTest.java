/*
 * Copyright 2012 Robert Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.modules.swing;

import org.aftersong.modules.swing.LookAndFeel;
import static org.testng.Assert.*;

import javax.swing.UIManager;

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
		LookAndFeel.chooseLookAndFeel(null);
	}
}
