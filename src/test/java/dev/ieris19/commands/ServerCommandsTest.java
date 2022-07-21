package dev.ieris19.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ServerCommandsTest {
	private ServerCommands commands = new ServerCommands();

	@Test void serverIPTest() {
		assertDoesNotThrow(commands::publicServerMachineIP);
		System.out.println(commands.publicServerMachineIP());
	}
}