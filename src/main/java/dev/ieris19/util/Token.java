package dev.ieris19.util;

import lib.ieris19.util.properties.FileProperties;

public class Token {
	private static String TOKEN;

	static {
			TOKEN = FileProperties.getInstance("sensitive").getProperty("api-token");
	}

	public static String get() {
		return TOKEN;
	}
}
