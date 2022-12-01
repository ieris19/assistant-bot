package com.ieris19.discord;

/**
 * Entry point of the application
 */
public class Main {
	/**
	 * A name for the application to be used in the logs
	 */
	public String APP_NAME = "IerisAssistantBot";

	/**
	 * Main method (entry point) of the application
	 *
	 * @param args command line arguments that will be ignored
	 */
	public static void main(String[] args) {
		
		Thread.currentThread().setName("Initializer-Thread");
		try {
			int errorLevel = Integer.parseInt(args[0]);
			Log.getInstance().setLogLevel(errorLevel);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {}
		Bot.start();
	}
}
