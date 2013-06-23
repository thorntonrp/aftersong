/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.core;

/**
 *
 * @author Robert P. Thornton
 */
public class CallStack {

	public static Class<?> getCallingClass() {
		return CallStackProvider.getInstance().getCallingClass();
	}

	private static class CallStackProvider extends SecurityManager {

		private static final CallStackProvider INSTANCE = new CallStackProvider();

		static CallStackProvider getInstance() {
			return INSTANCE;
		}

		Class<?>[] getCallStack() {
			return super.getClassContext();
		}

		Class<?> getCallingClass() {
			Class<?> contextClass = CallStack.class;
			for (Class<?> caller : getCallStack()) {
				String callerName = getOuterClassName(caller);
				if (!callerName.equals(contextClass.getName())) {
					if (contextClass == CallStack.class) {
						contextClass = caller;
					} else {
						return caller;
					}
				}
			}
			return null;
		}

		private String getOuterClassName(Class<?> caller) {
			return caller.getName().split("\\$")[0];
		}
	}

	private CallStack() {
	}
}
