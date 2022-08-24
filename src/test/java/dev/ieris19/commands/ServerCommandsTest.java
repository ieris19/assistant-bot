package dev.ieris19.commands;

import dev.ieris19.util.CommandUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ServerCommandsTest {
	private CommandUtils commands = new CommandUtils();

	@Test void serverIPTest() {
		assertDoesNotThrow(CommandUtils::publicServerMachineIP);
		System.out.println(CommandUtils.publicServerMachineIP());
	}
}