package com.ieris19.discord.util;

import com.ieris19.lib.common.cli.TextColor;
import com.ieris19.lib.files.config.FileProperties;
import com.ieris19.lib.util.log.core.IerisLog;

import java.util.Scanner;

/**
 * Class container for the token
 */
public class Token {
	/**
	 * API token
	 */
	private static String TOKEN;

	static {
		try {
			TOKEN = FileProperties.getInstance("token").getProperty("api-token");
		} catch (IllegalArgumentException e) {
			IerisLog.getInstance().error("There is no such property in the config file");
			IerisLog.getInstance().info("Creating token property");
			Scanner input = new Scanner(System.in);
			IerisLog.getInstance().info("Please enter your token");
			TextColor.print("Please enter your token ", TextColor.YELLOW);
			FileProperties.getInstance("token").createProperty("api-token", input.nextLine());
			TOKEN = FileProperties.getInstance("token").getProperty("api-token");
		}
	}

	/**
	 * Returns the token
	 */
	public static String get() {
		return TOKEN;
	}
}
