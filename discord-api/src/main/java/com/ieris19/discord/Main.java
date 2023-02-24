package com.ieris19.discord;

import com.ieris19.lib.util.log.core.IerisLog;

/**
 * Entry point of the application
 */
public class Main {
	/**
	 * A name for the application to be used in the logs
	 */
	public static String APP_NAME = "IerisAssistantBot";

	/**
	 * Main method (entry point) of the application
	 *
	 * @param args command line arguments that will be ignored
	 */
	public static void main(String[] args) {
		IerisLog.getInstance(APP_NAME);
		Thread.currentThread().setName("Initializer-Thread");
		try {
			int errorLevel = Integer.parseInt(args[0]);
			IerisLog.getInstance().setLogLevel(errorLevel);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {}
		Bot.start();
	}
}
