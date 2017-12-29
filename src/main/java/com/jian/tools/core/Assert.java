package com.jian.tools.core;

import java.util.Collection;
import java.util.Map;

public abstract class Assert {

	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}


	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void hasLength(String text, String message) {
		if (Tools.isNullOrEmpty(text)) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void notEmpty(Object[] array, String message) {
		if (array.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void notEmpty(Collection<?> collection, String message) {
		if (collection.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}


	public static void notEmpty(Map<?, ?> map, String message) {
		if (map.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}


}
