package dev.ieris19;

import lib.ieris19.util.log.Log;


/**
 * Entry point of the application
 */
public class Start {
	/**
	 * Main method (entry point) of the application
	 *
	 * @param args command line arguments that will be ignored
	 */
	public static void main(String[] args) {
		Log.getInstance().setName("IerisAssistantBot");
		Thread.currentThread().setName("Initializer-Thread");
		try {
			int errorLevel = Integer.parseInt(args[0]);
			Log.getInstance().setLogLevel(errorLevel);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {}
		Bot.start();
	}
}
