package dev.ieris19;

import lib.ieris19.util.log.Log;


public class Start {
	public static void main(String[] args) {
		Log.getInstance().setName("IerisAssistantBot");
		Thread.currentThread().setName("Initializer-Thread");
		Bot assistant = Bot.getInstance();
	}
}
