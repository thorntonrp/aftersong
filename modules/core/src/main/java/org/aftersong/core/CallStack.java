/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.core;

/**
 * Provides utilities for introspecting the call stack.
 *
 * @author Robert P. Thornton
 */
public class CallStack {

	public static Class<?> getCallingClass() {
		return CallStackProvider.getInstance().getCallingClass(0);
	}

	public static Class<?> getCallingClass(int offset) {
		return CallStackProvider.getInstance().getCallingClass(offset);
	}

	private static class CallStackProvider extends SecurityManager {

		private static final CallStackProvider INSTANCE = new CallStackProvider();

		static CallStackProvider getInstance() {
			return INSTANCE;
		}

		Class<?>[] getCallStack() {
			return super.getClassContext();
		}

		Class<?> getCallingClass(int offset) {
			Class<?> contextClass = CallStack.class;
			Class[] callStack = getCallStack();
			for (int i = 0; i < callStack.length; i++) {
				Class<?> caller = callStack[i];
				String callerName = getOuterClassName(caller);
				if (!callerName.equals(contextClass.getName())) {
					if (contextClass == CallStack.class) {
						contextClass = caller;
					} else {
						return callStack[i + offset];
					}
				}
			}
			for (Class<?> caller : getCallStack()) {
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
